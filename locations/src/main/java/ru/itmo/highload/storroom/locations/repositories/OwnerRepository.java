package ru.itmo.highload.storroom.locations.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.locations.models.OwnerEntity;

import java.util.Collection;
import java.util.UUID;

public interface OwnerRepository extends R2dbcRepository<OwnerEntity, UUID> {
    Mono<Boolean> existsByName(String name);
    Flux<OwnerEntity> findAllByIdIn(Collection<UUID> id);
}
