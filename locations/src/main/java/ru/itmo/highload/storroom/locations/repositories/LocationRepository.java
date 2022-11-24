package ru.itmo.highload.storroom.locations.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.locations.models.LocationEntity;

import java.util.Collection;
import java.util.UUID;

public interface LocationRepository extends R2dbcRepository<LocationEntity, UUID> {
    Mono<Boolean> existsByAddress(String address);
    Flux<LocationEntity> findAllByIdIn(Collection<UUID> id);
}
