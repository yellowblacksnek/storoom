package ru.itmo.highload.storoom.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import static ru.itmo.highload.storoom.models.DTOs.LockDTO;
import static ru.itmo.highload.storoom.models.DTOs.LockFullDTO;
import static ru.itmo.highload.storoom.models.DTOs.ManufacturerDTO;
import ru.itmo.highload.storoom.services.LockService;

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
    public LockFullDTO create(@RequestBody LockDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}/manufacturer")
    @ResponseStatus(HttpStatus.OK)
    public LockFullDTO updateManufacturer(@PathVariable UUID id, @RequestBody ManufacturerDTO dto) {
        return service.updateManufacturer(id, dto.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LockFullDTO deleteById(@PathVariable UUID id) {
        return service.deleteById(id);
    }
}
