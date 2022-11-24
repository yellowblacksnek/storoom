package ru.itmo.highload.storroom.locations.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.locations.dtos.LocationCompactDTO;
import ru.itmo.highload.storroom.locations.dtos.LocationDTO;
import ru.itmo.highload.storroom.locations.dtos.LocationReadDTO;
import ru.itmo.highload.storroom.locations.services.LocationService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping("{id}")
    public Mono<LocationReadDTO> getById(@PathVariable UUID id) {
        return locationService.getById(id);
    }

    @GetMapping
    public Flux<LocationCompactDTO> getAllLocations(Pageable pageable) {
        return locationService.getAll(pageable);
    }

    @PostMapping
    public Mono<LocationReadDTO> addLocation(@RequestBody LocationDTO req) {
        return locationService.create(req);
    }

    @PutMapping("{locationId}/owners/{ownerId}")
    public Mono<LocationReadDTO> addOwnerToLocation(@PathVariable UUID locationId, @PathVariable UUID ownerId) {
        return locationService.addOwner(locationId, ownerId);
    }

    @DeleteMapping("{locationId}/owners/{ownerId}")
    public Mono<LocationReadDTO> deleteOwnerFromLocation(@PathVariable UUID locationId, @PathVariable UUID ownerId) {
        return locationService.deleteOwner(locationId, ownerId);
    }

    @PutMapping("{id}")
    public Mono<LocationReadDTO> updateLocation(@PathVariable String id, @RequestBody LocationDTO dto) {
        return locationService.update(UUID.fromString(id), dto);
    }

    @DeleteMapping("{id}")
    public Mono<LocationReadDTO> deleteLocation(@PathVariable("id") String id) {
        return locationService.delete(UUID.fromString(id));
    }
}
