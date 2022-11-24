package ru.itmo.highload.storroom.locations.repositories;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.locations.models.OwnerLocationEntity;

import java.util.UUID;

public interface OwnerLocationRepository{
    Flux<OwnerLocationEntity> findByLocationId(UUID locationId);
    Flux<OwnerLocationEntity> findByOwnerId(UUID ownerId);
    Mono<OwnerLocationEntity> add(UUID ownerId, UUID locationId);
    Mono<OwnerLocationEntity> delete(UUID ownerId, UUID locationId);
}
