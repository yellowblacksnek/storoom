package ru.itmo.highload.storromm.aggregator.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storromm.aggregator.annotations.CustomizedOperation;
import ru.itmo.highload.storromm.aggregator.clients.LocationClient;
import ru.itmo.highload.storromm.aggregator.dtos.locations.LocationCompactDTO;
import ru.itmo.highload.storromm.aggregator.dtos.locations.LocationDTO;
import ru.itmo.highload.storromm.aggregator.dtos.locations.LocationReadDTO;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/locations")
@PreAuthorize("hasAuthority('superuser')")
@RequiredArgsConstructor
@Tag(name = "Locations")
public class LocationController {
    private final LocationClient locationClient;

    @GetMapping("{id}")
    @CustomizedOperation(description = "Get location by id.", responseCodes = {400, 401, 403, 404})
    public Mono<LocationReadDTO> getLocation(@PathVariable UUID id) { return locationClient.getLocation(id); }

    @GetMapping
    @CustomizedOperation(description = "Get all locations.", pageable = true, responseCodes = {401, 403})
    public Flux<LocationCompactDTO> getLocations(Pageable pageable) {
        return locationClient.getAllLocations(pageable);
    }

    @PostMapping
    @CustomizedOperation(description = "Add location.", responseCodes = {400, 401, 403})
    public Mono<LocationReadDTO> addLocation(@RequestBody LocationDTO req) {
        return locationClient.addLocation(req);
    }

    @PutMapping("{locationId}/owners/{ownerId}")
    @CustomizedOperation(description = "Add owner to location.", responseCodes = {400, 401, 403, 404, 409})
    public Mono<LocationReadDTO> addOwnerToLocation(@PathVariable UUID locationId, @PathVariable UUID ownerId) {
        return locationClient.addOwnerToLocation(locationId, ownerId);
    }

    @DeleteMapping("{locationId}/owners/{ownerId}")
    @CustomizedOperation(description = "Delete owner from location.", responseCodes = {400, 401, 403, 404, 409})
    public Mono<LocationReadDTO> deleteOwnerFromLocation(@PathVariable UUID locationId, @PathVariable UUID ownerId) {
        return locationClient.deleteOwnerFromLocation(locationId, ownerId);
    }

    @PutMapping("{id}")
    @CustomizedOperation(description = "Update location's info.", responseCodes = {400, 401, 403, 404, 409})
    public Mono<LocationReadDTO> updateLocation(@PathVariable String id, @RequestBody LocationDTO dto) {
        return locationClient.updateLocation(id, dto);
    }

    @DeleteMapping("{id}")
    @CustomizedOperation(description = "Delete location.", responseCodes = {400, 401, 403, 404, 409})
    public Mono<LocationReadDTO> deleteLocation(@PathVariable("id") String id) {
        return locationClient.deleteLocation(id);
    }
}
