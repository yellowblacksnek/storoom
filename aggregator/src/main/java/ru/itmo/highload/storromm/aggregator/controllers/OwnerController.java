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
@RequestMapping("/owners")
@PreAuthorize("hasAuthority('superuser')")
@RequiredArgsConstructor
public class OwnerController {
    private final LocationClient ownerClient;

    @GetMapping
    public Flux<Object> getAllOwners(Pageable pageable) {
        return ownerClient.getAllOwners(pageable);
    }

    @PostMapping
    public Mono<Object> addOwner(@RequestBody Map<String,String> req) {
        return ownerClient.addOwner(req);
    }

    @PostMapping("{ownerId}/locations/{locationId}")
    public Mono<Object> addLocationToOwner(@PathVariable UUID ownerId, @PathVariable UUID locationId) {
        return ownerClient.addLocationToOwner(ownerId, locationId);
    }

    @DeleteMapping("{ownerId}/locations/{locationId}")
    public Mono<Object> deleteLocationFromOwner(@PathVariable UUID ownerId, @PathVariable UUID locationId) {
        return ownerClient.deleteLocationFromOwner(ownerId, locationId);
    }

    @PutMapping("{id}")
    public Mono<Object> updateOwner(@PathVariable String id, @RequestBody Map<String,String> dto) {
        return ownerClient.updateOwner(id, dto);
    }

    @DeleteMapping("{id}")
    public Mono<Object> deleteOwner(@PathVariable("id") String id) {
        return ownerClient.deleteOwner(id);
    }
}