package ru.itmo.highload.storoom.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storoom.services.UnitService;

import java.util.UUID;

import static ru.itmo.highload.storoom.models.DTOs.UnitDTO;
import static ru.itmo.highload.storoom.models.DTOs.UnitFullDTO;

@RestController
@RequestMapping("/units")
@RequiredArgsConstructor
public class UnitController {

    private final UnitService service;

    @GetMapping
    public Page<UnitFullDTO> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('superuser')")
    @ResponseStatus(HttpStatus.CREATED)
    public UnitFullDTO create(@RequestBody UnitDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    @ResponseStatus(HttpStatus.OK)
    public UnitFullDTO updateInfo(@PathVariable UUID id, @RequestBody UnitDTO dto) {
        return service.updateInfo(id, dto);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('superuser')")
    @ResponseStatus(HttpStatus.OK)
    public UnitFullDTO updateStatus(@PathVariable UUID id, @RequestBody UnitDTO dto) {
        return service.updateStatus(id, dto.getStatus());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    @ResponseStatus(HttpStatus.OK)
    public UnitFullDTO delete(@PathVariable UUID id) {
        return service.delete(id);
    }
}
