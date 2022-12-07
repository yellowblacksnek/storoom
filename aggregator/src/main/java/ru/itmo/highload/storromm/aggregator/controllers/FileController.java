package ru.itmo.highload.storromm.aggregator.controllers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


import java.util.List;

@Slf4j
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    @Data
    private static class FileDTO {
        private String username;
        private String filename;
    }
    /*

        dummy controller for docs generation only

     */

    @GetMapping
    public Mono<List<FileDTO>> getFiles() {
        return null;
    }

    @GetMapping("/{username}")
    public Mono<List<FileDTO>> getFilesByUser(@PathVariable String username) {
        return null;
    }

    @GetMapping("/{username}/{filename}")
    public Mono<InputStreamResource> getFile(@PathVariable String username, @PathVariable String filename) {
        return null;
    }

    @PostMapping("/{username}/{filename}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<FileDTO> upload(@PathVariable String username,
                                @PathVariable String filename,
                                @RequestPart(name = "file") FilePart filePart) {
        return null;
    }

    @RequestMapping(value = "/{username}/{filename}", method = RequestMethod.DELETE)
    public Mono<Boolean> deleteFile(@PathVariable String username, @PathVariable String filename) {
        return null;
    }
}
