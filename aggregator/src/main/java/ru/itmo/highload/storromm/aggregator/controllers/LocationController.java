package ru.itmo.highload.storromm.aggregator.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storromm.aggregator.clients.LocationClient;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/locations")
@PreAuthorize("hasAuthority('superuser')")
@RequiredArgsConstructor
public class LocationController {
    private final LocationClient locationClient;

    @GetMapping("{id}")
    public Mono<Object> getLocation(@PathVariable UUID id) { return locationClient.getLocation(id); }

    @GetMapping
    public Flux<Object> getAllLocations(Pageable pageable) {
        return locationClient.getAllLocations(pageable);
    }

    @PostMapping
    public Mono<Object> addLocation(@RequestBody Map<String,String> req) {
        return locationClient.addLocation(req);
    }

    @PutMapping("{locationId}/owners/{ownerId}")
    public Mono<Object> addOwnerToLocation(@PathVariable UUID locationId, @PathVariable UUID ownerId) {
        return locationClient.addOwnerToLocation(locationId, ownerId);
    }

    @DeleteMapping("{locationId}/owners/{ownerId}")
    public Mono<Object> deleteOwnerFromLocation(@PathVariable UUID locationId,@PathVariable UUID ownerId) {
        return locationClient.deleteOwnerFromLocation(locationId, ownerId);
    }

    @PutMapping("{id}")
    public Mono<Object> updateLocation(@PathVariable String id, @RequestBody Map<String,String> dto) {
        return locationClient.updateLocation(id, dto);
    }

    @DeleteMapping("{id}")
    public Mono<Object> deleteLocation(@PathVariable("id") String id) {
        return locationClient.deleteLocation(id);
    }
}
