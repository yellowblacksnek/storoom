package ru.itmo.highload.storroom.locations.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.locations.models.CompanyEntity;

import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface CompanyRepository extends ReactiveCrudRepository<CompanyEntity, UUID> {

    Flux<CompanyEntity> findAllBy(Pageable pageable);
    Mono<Boolean> existsByName(String name);
}
