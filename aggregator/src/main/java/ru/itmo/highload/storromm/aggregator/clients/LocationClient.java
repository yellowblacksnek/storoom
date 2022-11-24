package ru.itmo.highload.storromm.aggregator.clients;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@ReactiveFeignClient(name = "LOCATIONS-SERVICE")
public interface LocationClient {
    @GetMapping("/companies")
    Flux<Object> getAllCompanies(Pageable pageable);

    @PostMapping("/companies")
    Mono<Object> addCompany(@RequestBody Map<String,String> req);

    @PutMapping("/companies/{id}")
    Mono<Object> updateCompany(@PathVariable String id, @RequestBody Map<String,String> dto);

    @DeleteMapping("/companies/{id}")
    Mono<Object> deleteCompany(@PathVariable("id") String id);

    @GetMapping("/locations/{id}")
    Mono<Object> getLocation(@PathVariable UUID id);

    @GetMapping("/locations")
    Flux<Object> getAllLocations(Pageable pageable);

    @PostMapping("/locations")
    Mono<Object> addLocation(@RequestBody Map<String,String> req);

    @PutMapping("/locations/{locationId}/owners/{ownerId}")
    Mono<Object> addOwnerToLocation(@PathVariable UUID locationId,@PathVariable UUID ownerId);

    @DeleteMapping("/locations/{locationId}/owners/{ownerId}")
    Mono<Object> deleteOwnerFromLocation(@PathVariable UUID locationId,@PathVariable UUID ownerId);

    @PutMapping("/locations/{id}")
    Mono<Object> updateLocation(@PathVariable String id, @RequestBody Map<String,String> dto);

    @DeleteMapping("/locations/{id}")
    Mono<Object> deleteLocation(@PathVariable("id") String id);



    @GetMapping("/owners")
    Flux<Object> getAllOwners(Pageable pageable);

    @PostMapping("/owners")
    Mono<Object> addOwner(@RequestBody Map<String,String> req);

    @PutMapping("/owners/{ownerId}/locations/{locationId}")
    Mono<Object> addLocationToOwner(@PathVariable UUID ownerId, @PathVariable UUID locationId);

    @DeleteMapping("/owners/{ownerId}/locations/{locationId}")
    Mono<Object> deleteLocationFromOwner(@PathVariable UUID ownerId,@PathVariable UUID locationId);

    @PutMapping("/owners/{id}")
    Mono<Object> updateOwner(@PathVariable String id, @RequestBody Map<String,String> dto);

    @DeleteMapping("/owners/{id}")
    Mono<Object> deleteOwner(@PathVariable("id") String id);

}
