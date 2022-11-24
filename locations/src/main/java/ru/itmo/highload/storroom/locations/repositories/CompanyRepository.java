package ru.itmo.highload.storroom.locations.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.locations.models.CompanyEntity;

import java.util.UUID;

public interface CompanyRepository extends R2dbcRepository<CompanyEntity, UUID> {
    Mono<Boolean> existsByName(String name);
}
