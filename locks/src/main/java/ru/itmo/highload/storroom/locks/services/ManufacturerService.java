package ru.itmo.highload.storroom.locks.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.itmo.highload.storroom.locks.dtos.ManufacturerDTO;
import ru.itmo.highload.storroom.locks.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storroom.locks.repositories.ManufacturerRepo;
import ru.itmo.highload.storroom.locks.utils.Mapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManufacturerService {
    private final ManufacturerRepo repo;

    public Flux<ManufacturerDTO> getAll(Pageable pageable) {
        return Flux.fromIterable(repo.findAll(pageable).map(Mapper::toManufacturerDTO));
    }

    public Mono<ManufacturerDTO> getById(UUID id) {
        return Mono.just(Mapper.toManufacturerDTO(repo.findById(id).orElseThrow(ResourceNotFoundException::new)));
    }

    public Mono<ManufacturerDTO> create(ManufacturerDTO dto) {
        return Mono.just(Mapper.toManufacturerDTO(repo.save(Mapper.toManufacturerEntity(dto))));
    }

    public Mono<ManufacturerDTO> updateName(UUID id, String name) {
        return Mono.just(repo.findById(id).orElseThrow(ResourceNotFoundException::new))
                .publishOn(Schedulers.boundedElastic())
                .map(entity -> {
                    entity.setName(name);
                    entity = repo.save(entity);
                    return Mapper.toManufacturerDTO(entity);
                });

//        ManufacturerEntity entity = repo.findById(id).orElseThrow(ResourceNotFoundException::new);
//        entity.setName(name);
//        entity = repo.save(entity);
//        return Mapper.toManufacturerDTO(entity);
    }

    public Mono<ManufacturerDTO> deleteById(UUID id) {
        return Mono.just(repo.findById(id).orElseThrow(ResourceNotFoundException::new))
                .publishOn(Schedulers.boundedElastic())
                .map(entity -> {
                    repo.delete(entity);
                    return Mapper.toManufacturerDTO(entity);
                });
//        ManufacturerEntity entity = repo.findById(id).orElseThrow(ResourceNotFoundException::new);
//        repo.delete(entity);
//        return Mapper.toManufacturerDTO(entity);
    }
}
