package ru.itmo.highload.storromm.aggregator.clients;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storromm.aggregator.dtos.companies.CompanyDTO;
import ru.itmo.highload.storromm.aggregator.dtos.companies.CompanyReadDTO;
import ru.itmo.highload.storromm.aggregator.dtos.locations.LocationCompactDTO;
import ru.itmo.highload.storromm.aggregator.dtos.locations.LocationDTO;
import ru.itmo.highload.storromm.aggregator.dtos.locations.LocationReadDTO;
import ru.itmo.highload.storromm.aggregator.dtos.owners.OwnerCompactDTO;
import ru.itmo.highload.storromm.aggregator.dtos.owners.OwnerDTO;
import ru.itmo.highload.storromm.aggregator.dtos.owners.OwnerInfoDTO;
import ru.itmo.highload.storromm.aggregator.dtos.owners.OwnerReadDTO;

import java.util.UUID;

@ReactiveFeignClient(name = "LOCATIONS-SERVICE")
public interface LocationClient {
    @GetMapping("/companies")
    Flux<CompanyReadDTO> getAllCompanies(Pageable pageable);

    @PostMapping("/companies")
    Mono<CompanyReadDTO> addCompany(@RequestBody CompanyDTO req);

    @PutMapping("/companies/{id}")
    Mono<CompanyReadDTO> updateCompany(@PathVariable String id, @RequestBody CompanyDTO dto);

    @DeleteMapping("/companies/{id}")
    Mono<CompanyReadDTO> deleteCompany(@PathVariable("id") String id);

    @GetMapping("/locations/{id}")
    Mono<LocationReadDTO> getLocation(@PathVariable UUID id);

    @GetMapping("/locations")
    Flux<LocationCompactDTO> getAllLocations(Pageable pageable);

    @PostMapping("/locations")
    Mono<LocationReadDTO> addLocation(@RequestBody LocationDTO req);

    @PutMapping("/locations/{locationId}/owners/{ownerId}")
    Mono<LocationReadDTO> addOwnerToLocation(@PathVariable UUID locationId,@PathVariable UUID ownerId);

    @DeleteMapping("/locations/{locationId}/owners/{ownerId}")
    Mono<LocationReadDTO> deleteOwnerFromLocation(@PathVariable UUID locationId,@PathVariable UUID ownerId);

    @PutMapping("/locations/{id}")
    Mono<LocationReadDTO> updateLocation(@PathVariable String id, @RequestBody LocationDTO dto);

    @DeleteMapping("/locations/{id}")
    Mono<LocationReadDTO> deleteLocation(@PathVariable("id") String id);



    @GetMapping("/owners")
    Flux<OwnerCompactDTO> getAllOwners(Pageable pageable);

    @PostMapping("/owners")
    Mono<OwnerReadDTO> addOwner(@RequestBody OwnerDTO req);

    @PutMapping("/owners/{ownerId}/locations/{locationId}")
    Mono<OwnerReadDTO> addLocationToOwner(@PathVariable UUID ownerId, @PathVariable UUID locationId);

    @DeleteMapping("/owners/{ownerId}/locations/{locationId}")
    Mono<OwnerReadDTO> deleteLocationFromOwner(@PathVariable UUID ownerId,@PathVariable UUID locationId);

    @PutMapping("/owners/{id}")
    Mono<OwnerReadDTO> updateOwner(@PathVariable String id, @RequestBody OwnerInfoDTO dto);

    @DeleteMapping("/owners/{id}")
    Mono<OwnerReadDTO> deleteOwner(@PathVariable("id") String id);

}
