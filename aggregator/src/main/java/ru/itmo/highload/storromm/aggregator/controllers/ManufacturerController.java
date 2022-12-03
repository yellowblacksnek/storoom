package ru.itmo.highload.storromm.aggregator.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storromm.aggregator.clients.LockClient;
import ru.itmo.highload.storromm.aggregator.dtos.manufacturers.ManufacturerDTO;
import ru.itmo.highload.storromm.aggregator.dtos.manufacturers.ManufacturerNameDTO;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {
    private final LockClient manufacturerClient;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    public Mono<ManufacturerDTO> getById(@PathVariable UUID id) {
        return manufacturerClient.getManufacturerById(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('superuser')")
    public Mono<ManufacturerDTO> getAll(Pageable pageable) {
        return manufacturerClient.getAllManufacturers(pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('superuser')")
    public Mono<ManufacturerDTO> create(@RequestBody ManufacturerDTO dto) {
        return manufacturerClient.createManufacturer(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    public Mono<ManufacturerDTO> updateName(@PathVariable UUID id, @RequestBody ManufacturerNameDTO dto) {
        return manufacturerClient.updateManufacturerName(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    public Mono<ManufacturerDTO> deleteById(@PathVariable UUID id) {
        return manufacturerClient.deleteManufacturer(id);
    }
}
