package ru.itmo.highload.storroom.aggregatorreactive.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.aggregatorreactive.clients.FileClient;
import ru.itmo.highload.storroom.aggregatorreactive.dtos.files.FileDTO;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    final FileClient client;
//    final FileService service;

    @GetMapping
    public Mono<List<FileDTO>> getFiles() {
        log.info("getFiles");
        return client.getFiles();
    }

    @GetMapping("/{username}")
    public Mono<List<FileDTO>> getFilesByUser(@PathVariable String username) {
        log.info("getFilesByUser {}", username);
        return client.getFilesByUser(username);
    }

    @GetMapping("/{username}/{filename}")
    public Mono<InputStreamResource> getFile(@PathVariable String username, @PathVariable String filename) {
        log.info("getFile {} {}", username, filename);
        return client.getFile(username, filename);
    }

//    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping("/{username}/{filename}")
    public Mono<FileDTO> upload(@PathVariable String username,
                                @PathVariable String filename,
                                @RequestPart(name = "file") FilePart filePart)  {
        log.info("upload {}/{}", username,filename);
        return client.upload(username, filename, filePart);
    }

    @RequestMapping(value = "/{username}/{filename}", method = RequestMethod.DELETE)
    public Mono<Boolean> deleteFile(@PathVariable String username, @PathVariable String filename) {
        log.info("delete {} {}", username, filename);
        return client.deleteFile(username, filename);
    }
}
