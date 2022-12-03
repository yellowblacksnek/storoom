package ru.itmo.highload.storroom.locks.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.itmo.highload.storroom.locks.dtos.manufacturers.ManufacturerDTO;
import ru.itmo.highload.storroom.locks.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storroom.locks.repositories.ManufacturerRepo;
import ru.itmo.highload.storroom.locks.utils.Mapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManufacturerService {
    private final ManufacturerRepo repo;

    public Mono<Page<ManufacturerDTO>> getAll(Pageable pageable) {
        return Mono.fromCallable(() -> repo.findAll(pageable).map(Mapper::toManufacturerDTO))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<ManufacturerDTO> getById(UUID id) {
        return Mono.fromCallable(() -> Mapper.toManufacturerDTO(
                    repo.findById(id).orElseThrow(() ->
                            new ResourceNotFoundException("manufacturer", id.toString()))))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<ManufacturerDTO> create(ManufacturerDTO dto) {
        if (dto.getName() == null || dto.getName().isEmpty()) throw new IllegalArgumentException("name is empty");
        return Mono.fromCallable(() ->Mapper.toManufacturerDTO(repo.save(Mapper.toManufacturerEntity(dto))))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<ManufacturerDTO> updateName(UUID id, String name) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("name is empty");
        return Mono.fromCallable(() -> repo.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("manufacturer", id.toString())))
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
        return Mono.fromCallable(() -> repo.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("manufacturer", id.toString())))
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
