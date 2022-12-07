package ru.itmo.highload.storroom.aggregatorreactive.clients;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.aggregatorreactive.dtos.files.FileDTO;

import java.util.List;

//@ReactiveFeignClient(value = "FILES-SERVICE", url = "http://localhost:8086")
@ReactiveFeignClient(name = "FILES-SERVICE")
public interface FileClient {

    @GetMapping("/files")
    Mono<List<FileDTO>> getFiles();

    @GetMapping("/files/{username}")
    Mono<List<FileDTO>> getFilesByUser(@PathVariable String username);

    @GetMapping("/files/{username}/{filename}")
    Mono<InputStreamResource> getFile(@PathVariable String username, @PathVariable String filename);

//    @RequestMapping(value = "/files", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(value = "/files/{username}/{filename}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Mono<FileDTO> upload(@PathVariable String username, @PathVariable String filename, @RequestPart FilePart file);

    @RequestMapping(value = "/files/{username}/{filename}", method = RequestMethod.DELETE)
    Mono<Boolean> deleteFile(@PathVariable String username, @PathVariable String filename);
}
