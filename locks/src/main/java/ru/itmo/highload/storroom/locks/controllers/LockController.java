package ru.itmo.highload.storroom.locks.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.locks.dtos.locks.LockDTO;
import ru.itmo.highload.storroom.locks.dtos.locks.LockFullDTO;
import ru.itmo.highload.storroom.locks.dtos.locks.LockInfoDTO;
import ru.itmo.highload.storroom.locks.services.LockService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/locks")
@RequiredArgsConstructor
public class LockController {
    private final LockService service;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAuthority('superuser')")
    public Mono<LockFullDTO> getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAuthority('superuser')")
    public Mono<Page<LockFullDTO>> getAll(@ParameterObject Pageable pageable) {
        return service.getAll(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAuthority('superuser')")
    public Mono<LockFullDTO> create(@RequestBody LockDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAuthority('superuser')")
    public Mono<LockFullDTO> update(@PathVariable UUID id, @RequestBody LockInfoDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAuthority('superuser')")
    public Mono<LockFullDTO> deleteById(@PathVariable UUID id) {
        return service.deleteById(id);
    }
}
