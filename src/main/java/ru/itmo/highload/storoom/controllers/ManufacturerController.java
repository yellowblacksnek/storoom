package ru.itmo.highload.storoom.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import static ru.itmo.highload.storoom.models.DTOs.ManufacturerDTO;
import ru.itmo.highload.storoom.services.ManufacturerService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {
    private final ManufacturerService service;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ManufacturerDTO getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ManufacturerDTO> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ManufacturerDTO create(@RequestBody ManufacturerDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}/name")
    @ResponseStatus(HttpStatus.OK)
    public ManufacturerDTO updateName(@PathVariable UUID id, @RequestBody ManufacturerDTO dto) {
        return service.updateName(id, dto.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ManufacturerDTO deleteById(@PathVariable UUID id) {
        return service.deleteById(id);
    }
}
