package ru.itmo.highload.storromm.aggregator.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storromm.aggregator.annotations.CustomizedOperation;
import ru.itmo.highload.storromm.aggregator.clients.LockClient;
import ru.itmo.highload.storromm.aggregator.dtos.manufacturers.ManufacturerDTO;
import ru.itmo.highload.storromm.aggregator.dtos.manufacturers.ManufacturerNameDTO;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/manufacturers")
@RequiredArgsConstructor
@Tag(name = "Manufacturers")
public class ManufacturerController {
    private final LockClient manufacturerClient;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    @CustomizedOperation(description = "Get manufacturer by id.", responseCodes = {400, 401, 403, 404})
    public Mono<ManufacturerDTO> getManufacturer(@PathVariable UUID id) {
        return manufacturerClient.getManufacturerById(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('superuser')")
    @CustomizedOperation(description = "Get all manufacturers.", pageable = true, responseCodes = {401, 403})
    public Mono<ManufacturerDTO> getManufacturers(Pageable pageable) {
        return manufacturerClient.getAllManufacturers(pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('superuser')")
    @CustomizedOperation(description = "Add manufacturer.", responseCodes = {400, 401, 403})
    public Mono<ManufacturerDTO> createManufacturer(@RequestBody ManufacturerDTO dto) {
        return manufacturerClient.createManufacturer(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    @CustomizedOperation(description = "Update manufacturer's name.", responseCodes = {400, 401, 403, 404, 409})
    public Mono<ManufacturerDTO> updateManufacturerName(@PathVariable UUID id, @RequestBody ManufacturerNameDTO dto) {
        return manufacturerClient.updateManufacturerName(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    @CustomizedOperation(description = "Delete manufacturer.", responseCodes = {400, 401, 403, 404, 409})
    public Mono<ManufacturerDTO> deleteManufacturer(@PathVariable UUID id) {
        return manufacturerClient.deleteManufacturer(id);
    }
}
