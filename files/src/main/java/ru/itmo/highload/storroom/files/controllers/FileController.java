package ru.itmo.highload.storroom.files.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.files.dtos.FileDTO;
import ru.itmo.highload.storroom.files.dtos.NotificationMessage;
import ru.itmo.highload.storroom.files.services.MinioService;
import ru.itmo.highload.storroom.files.services.NotificationService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class FileController {
    private final MinioService minioService;
    private final NotificationService notificationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<FileDTO>> getFiles() {
        return minioService.getListObjects("");
    }
    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<FileDTO>> getFilesByUser(@PathVariable String username) {
        return minioService.getListObjects(username);
    }

    @GetMapping("/{username}/{filename}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Mono<InputStreamResource>> getFile(@PathVariable String username, @PathVariable String filename) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(minioService.download(username, filename));
    }

    @PostMapping(value = "/upload")
    @ResponseStatus(HttpStatus.OK)
    public Mono<FileDTO> upload(@ModelAttribute FileDTO file) {
        return minioService.uploadFile(file)
                .flatMap(resDto -> {
                    boolean isOk = resDto.getObjectName() != null | !resDto.getObjectName().isEmpty();
                    String okMsg = String.format("File %s was uploaded", file.getRealFilename());
                    String errMsg = String.format("Err: File %s was NOT uploaded", file.getRealFilename());

                    NotificationMessage message =
                            NotificationMessage.builder()
                                    .username(file.getUsername())
                                    .timestamp(LocalDateTime.now())
                                    .message(isOk ? okMsg : errMsg)
                                    .build();
                    return notificationService.sendNotification(message)
                            .thenReturn(resDto);
                });
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Boolean> deleteFile(@RequestParam String username, @RequestParam String filename) {
        String objectName = username + "/" + filename;
        return minioService.deleteObject(objectName)
                .flatMap(isDeleted -> {
                    String okMsg = String.format("File %s was deleted", filename);
                    String errMsg = String.format("Err: File %s was NOT deleted", filename);

                    NotificationMessage message =
                            NotificationMessage.builder()
                                .username(username)
                                .timestamp(LocalDateTime.now())
                                .message(isDeleted ? okMsg : errMsg)
                            .build();
                    return notificationService.sendNotification(message)
                            .thenReturn(isDeleted);
                });
    }

}