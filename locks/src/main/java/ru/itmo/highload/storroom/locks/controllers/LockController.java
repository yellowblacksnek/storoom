package ru.itmo.highload.storroom.locks.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storroom.locks.dtos.LockDTO;
import ru.itmo.highload.storroom.locks.dtos.LockFullDTO;
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
    public LockFullDTO getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<LockFullDTO> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('superuser')")
    public LockFullDTO create(@RequestBody LockDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('superuser')")
    public LockFullDTO update(@PathVariable UUID id, @RequestBody LockDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('superuser')")
    public LockFullDTO deleteById(@PathVariable UUID id) {
        return service.deleteById(id);
    }
}
