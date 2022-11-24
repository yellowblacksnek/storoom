package ru.itmo.highload.storroom.locations.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.locations.models.OwnerEntity;

import java.util.Collection;
import java.util.UUID;

public interface OwnerRepository extends ReactiveCrudRepository<OwnerEntity, UUID> {
    Mono<Boolean> existsByName(String name);
    Flux<OwnerEntity> findAllByIdIn(Collection<UUID> id);
}
