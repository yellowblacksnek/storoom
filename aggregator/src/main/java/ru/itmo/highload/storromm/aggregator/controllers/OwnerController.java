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
import ru.itmo.highload.storromm.aggregator.dtos.owners.OwnerCompactDTO;
import ru.itmo.highload.storromm.aggregator.dtos.owners.OwnerDTO;
import ru.itmo.highload.storromm.aggregator.dtos.owners.OwnerInfoDTO;
import ru.itmo.highload.storromm.aggregator.dtos.owners.OwnerReadDTO;

import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("/owners")
@PreAuthorize("hasAuthority('superuser')")
@RequiredArgsConstructor
@Tag(name = "Owners")
public class OwnerController {
    private final LocationClient ownerClient;

    @GetMapping
    @CustomizedOperation(description = "Get all owners.", pageable = true, responseCodes = {401, 403})
    public Flux<OwnerCompactDTO> getOwners(Pageable pageable) {
        return ownerClient.getAllOwners(pageable);
    }

    @PostMapping
    @CustomizedOperation(description = "Add owner.", responseCodes = {400, 401, 403, 409})
    public Mono<OwnerReadDTO> addOwner(@RequestBody OwnerDTO req) {
        return ownerClient.addOwner(req);
    }

    @PostMapping("{ownerId}/locations/{locationId}")
    @CustomizedOperation(description = "Add location to owner.", responseCodes = {400, 401, 403, 404, 409})
    public Mono<OwnerReadDTO> addLocationToOwner(@PathVariable UUID ownerId, @PathVariable UUID locationId) {
        return ownerClient.addLocationToOwner(ownerId, locationId);
    }

    @DeleteMapping("{ownerId}/locations/{locationId}")
    @CustomizedOperation(description = "Delete location from owner.", responseCodes = {400, 401, 403, 404, 409})
    public Mono<OwnerReadDTO> deleteLocationFromOwner(@PathVariable UUID ownerId, @PathVariable UUID locationId) {
        return ownerClient.deleteLocationFromOwner(ownerId, locationId);
    }

    @PutMapping("{id}")
    @CustomizedOperation(description = "Update owner.", responseCodes = {400, 401, 403, 404, 409})
    public Mono<OwnerReadDTO> updateOwner(@PathVariable String id, @RequestBody OwnerInfoDTO dto) {
        return ownerClient.updateOwner(id, dto);
    }

    @DeleteMapping("{id}")
    @CustomizedOperation(description = "Delete owner.", responseCodes = {400, 401, 403, 404, 409})
    public Mono<OwnerReadDTO> deleteOwner(@PathVariable("id") String id) {
        return ownerClient.deleteOwner(id);
    }
}