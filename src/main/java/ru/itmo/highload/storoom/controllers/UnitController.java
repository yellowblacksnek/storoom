package ru.itmo.highload.storoom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storoom.services.UnitService;

import java.util.UUID;

import static ru.itmo.highload.storoom.models.DTOs.UnitDTO;

@RestController
@RequestMapping("/units")
public class UnitController {

    @Autowired
    private UnitService service;

    @GetMapping
    public Page<UnitDTO> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('superuser')")
    @ResponseStatus(HttpStatus.CREATED)
    public UnitDTO create(@RequestBody UnitDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    @ResponseStatus(HttpStatus.OK)
    public UnitDTO updateInfo(@PathVariable UUID id, @RequestBody UnitDTO dto) {
        return service.updateInfo(id, dto);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('superuser')")
    @ResponseStatus(HttpStatus.OK)
    public UnitDTO updateStatus(@PathVariable UUID id, @RequestBody UnitDTO dto) {
        return service.updateStatus(id, dto.getStatus());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
