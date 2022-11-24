package ru.itmo.highload.storroom.locations.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.locations.dtos.LocationCompactDTO;
import ru.itmo.highload.storroom.locations.dtos.OwnerCompactDTO;
import ru.itmo.highload.storroom.locations.models.OwnerLocationEntity;
import ru.itmo.highload.storroom.locations.repositories.LocationRepository;
import ru.itmo.highload.storroom.locations.repositories.OwnerLocationRepository;
import ru.itmo.highload.storroom.locations.repositories.OwnerRepository;
import ru.itmo.highload.storroom.locations.utils.Mapper;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OwnerLocationService {
    private final OwnerLocationRepository repo;
    private final OwnerRepository ownerRepo;
    private final LocationRepository locationRepo;

    public Flux<UUID> getOwnersIds(UUID locationId) {
        return repo.findByLocationId(locationId).map(OwnerLocationEntity::getOwnerId);
    }

    public Flux<UUID> getLocationsIds(UUID ownerId) {
        return repo.findByOwnerId(ownerId).map(OwnerLocationEntity::getLocationId);
    }

    public Mono<List<OwnerCompactDTO>> getOwnersCompact(UUID locationId) {
        return getOwnersIds(locationId).collectList()
                .flatMap(i -> ownerRepo.findAllByIdIn(i)
                        .map(Mapper::toOwnerCompactDTO).collectList());
    }

    public Mono<List<LocationCompactDTO>> getLocationsCompact(UUID ownerId) {
        return getLocationsIds(ownerId).collectList()
                .flatMap(i -> locationRepo.findAllByIdIn(i)
                        .map(Mapper::toLocationCompactDTO).collectList());
    }

    public Mono<OwnerLocationEntity> add(UUID ownerId, UUID locationId) {
        return repo.add(ownerId,locationId);
    }

    public Mono<OwnerLocationEntity> delete(UUID ownerId, UUID locationId) {
        return repo.delete(ownerId,locationId);
    }
}
