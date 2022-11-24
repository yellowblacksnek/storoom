package ru.itmo.highload.storroom.locations.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.locations.models.CompanyEntity;

import java.util.UUID;

public interface CompanyRepository extends ReactiveCrudRepository<CompanyEntity, UUID> {
    Mono<Boolean> existsByName(String name);
}
