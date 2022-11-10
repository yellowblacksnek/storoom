package ru.itmo.highload.storroom.locks.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.itmo.highload.storroom.locks.dtos.LockDTO;
import ru.itmo.highload.storroom.locks.dtos.LockFullDTO;
import ru.itmo.highload.storroom.locks.dtos.ManufacturerDTO;
import ru.itmo.highload.storroom.locks.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storroom.locks.models.LockEntity;
import ru.itmo.highload.storroom.locks.repositories.LockRepo;
import ru.itmo.highload.storroom.locks.utils.Mapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LockService {
    private final LockRepo repo;
    private final ManufacturerService manufacturerService;

    public Flux<LockFullDTO> getAll(Pageable pageable) {
        return Flux.fromIterable(repo.findAll(pageable).map(Mapper::toLockFullDTO));
    }

    public Mono<LockFullDTO> getById(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is empty");
        return Mono.fromCallable(() ->
                Mapper.toLockFullDTO(repo.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("lock", id.toString()))))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<LockFullDTO> create(LockDTO dto) {
        if (dto.getName() == null || dto.getName().isEmpty()) throw new IllegalArgumentException("name is empty");
        if (dto.getManufacturer() == null) throw new IllegalArgumentException("manufacturer is empty");

        return Mono.fromCallable(() -> manufacturerService.getById(dto.getManufacturer()))
                .map(manufacturer -> {
                    LockEntity entity = Mapper.toLockEntity(dto);
                    ManufacturerDTO manuDto = manufacturerService.getById(dto.getManufacturer()).block();
                    if(manuDto == null) throw new ResourceNotFoundException("manufacturer", dto.getManufacturer().toString());
                    entity.setManufacturer(Mapper.toManufacturerEntity(manuDto));
                    return Mapper.toLockFullDTO(repo.save(entity));
                }).subscribeOn(Schedulers.boundedElastic());

//        ManufacturerDTO manufacturer = manufacturerService.getById(dto.getManufacturer());
//        LockEntity entity = Mapper.toLockEntity(dto);
//        entity.setManufacturer(Mapper.toManufacturerEntity(manufacturer));
//        return Mapper.toLockFullDTO(repo.save(entity));
    }

    public Mono<LockFullDTO> update(UUID id, LockDTO dto) {
        if (dto.getName() == null || dto.getName().isEmpty()) throw new IllegalArgumentException("name is empty");
        if (dto.getManufacturer() == null) throw new IllegalArgumentException("manufacturer is empty");
        return
                Mono.fromCallable(() -> repo.findById(id).orElseThrow(() ->
                                new ResourceNotFoundException("lock", id.toString())))
                        .publishOn(Schedulers.boundedElastic())
                .map(entity -> {
                    entity.setName(dto.getName());
                    ManufacturerDTO manuDto = manufacturerService.getById(dto.getManufacturer()).block();
                    if(manuDto == null) throw new ResourceNotFoundException("manufacturer", dto.getManufacturer().toString());
                    entity.setManufacturer(Mapper.toManufacturerEntity(manuDto));
                    return Mapper.toLockFullDTO(repo.save(entity));
                });

//        LockEntity entity = repo.findById(id).orElseThrow(ResourceNotFoundException::new);
//        entity.setName(dto.getName());
//        entity.setManufacturer(Mapper.toManufacturerEntity(manufacturerService.getById(dto.getManufacturer())));
//        entity = repo.save(entity);
//        return Mapper.toLockFullDTO(entity);
    }

    public Mono<LockFullDTO> deleteById(UUID id) {
        return Mono.fromCallable(() -> repo.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("lock", id.toString())))
                .publishOn(Schedulers.boundedElastic())
                .map(entity -> {
                    repo.delete(entity);
                    return Mapper.toLockFullDTO(entity);
                });
//        LockEntity entity = repo.findById(id).orElseThrow(ResourceNotFoundException::new);
//        repo.delete(entity);
//        return Mapper.toLockFullDTO(entity);
    }
}
