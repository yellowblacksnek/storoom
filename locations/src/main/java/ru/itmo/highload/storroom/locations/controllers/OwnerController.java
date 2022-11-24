package ru.itmo.highload.storroom.locations.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.locations.dtos.OwnerCompactDTO;
import ru.itmo.highload.storroom.locations.dtos.OwnerDTO;
import ru.itmo.highload.storroom.locations.dtos.OwnerReadDTO;
import ru.itmo.highload.storroom.locations.services.OwnerService;

import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;

    @GetMapping("{id}")
    public Mono<OwnerReadDTO> getOwner(@PathVariable UUID id) {
        return ownerService.getById(id);
    }

    @GetMapping
    public Flux<OwnerCompactDTO> getAllOwners(Pageable pageable) {
        return ownerService.getAll(pageable);
    }

    @PostMapping
    public Mono<OwnerReadDTO> addOwner(@RequestBody OwnerDTO req) {
        return ownerService.create(req);
    }

    @PutMapping("{ownerId}/locations/{locationId}")
    public Mono<OwnerReadDTO> addLocationToOwner(@PathVariable UUID ownerId, @PathVariable UUID locationId) {
        return ownerService.addLocation(ownerId, locationId);
    }

    @DeleteMapping("{ownerId}/locations/{locationId}")
    public Mono<OwnerReadDTO> deleteLocationFromOwner(@PathVariable UUID ownerId, @PathVariable UUID locationId) {
        return ownerService.deleteLocation(ownerId, locationId);
    }

    @PutMapping("{id}")
    public Mono<OwnerReadDTO> updateOwner(@PathVariable UUID id, @RequestBody OwnerDTO dto) {
        return ownerService.update(id, dto);
    }

    @DeleteMapping("{id}")
    public Mono<OwnerReadDTO> deleteOwner(@PathVariable("id") UUID id) {
        return ownerService.delete(id);
    }
}