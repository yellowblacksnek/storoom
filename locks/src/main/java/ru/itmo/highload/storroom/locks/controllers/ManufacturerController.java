package ru.itmo.highload.storroom.locks.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.locks.dtos.ManufacturerDTO;
import ru.itmo.highload.storroom.locks.services.ManufacturerService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {
    private final ManufacturerService service;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ManufacturerDTO> getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<ManufacturerDTO> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('superuser')")
    public Mono<ManufacturerDTO> create(@RequestBody ManufacturerDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}/name")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('superuser')")
    public Mono<ManufacturerDTO> updateName(@PathVariable UUID id, @RequestBody ManufacturerDTO dto) {
        return service.updateName(id, dto.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('superuser')")
    public Mono<ManufacturerDTO> deleteById(@PathVariable UUID id) {
        return service.deleteById(id);
    }
}
