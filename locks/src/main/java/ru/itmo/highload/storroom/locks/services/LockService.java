package ru.itmo.highload.storroom.locks.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.itmo.highload.storroom.locks.dtos.LockDTO;
import ru.itmo.highload.storroom.locks.dtos.LockFullDTO;
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
        return Mono.just(Mapper.toLockFullDTO(repo.findById(id).orElseThrow(ResourceNotFoundException::new)));
    }

    public Mono<LockFullDTO> create(LockDTO dto) {
        return
                Mono.just(manufacturerService.getById(dto.getManufacturer()))
                .map(manufacturer -> {
                    LockEntity entity = Mapper.toLockEntity(dto);
                    entity.setManufacturer(Mapper.toManufacturerEntity(manufacturer.block()));
                    return Mapper.toLockFullDTO(repo.save(entity));
                });
//        ManufacturerDTO manufacturer = manufacturerService.getById(dto.getManufacturer());
//        LockEntity entity = Mapper.toLockEntity(dto);
//        entity.setManufacturer(Mapper.toManufacturerEntity(manufacturer));
//        return Mapper.toLockFullDTO(repo.save(entity));
    }

    public Mono<LockFullDTO> update(UUID id, LockDTO dto) {
        return
                Mono.just(repo.findById(id).orElseThrow(ResourceNotFoundException::new))
                        .publishOn(Schedulers.boundedElastic())
                .map(entity -> {
                    entity.setName(dto.getName());
                    entity.setManufacturer(Mapper.toManufacturerEntity(manufacturerService.getById(dto.getManufacturer()).block()));
                    return Mapper.toLockFullDTO(repo.save(entity));
                });

//        LockEntity entity = repo.findById(id).orElseThrow(ResourceNotFoundException::new);
//        entity.setName(dto.getName());
//        entity.setManufacturer(Mapper.toManufacturerEntity(manufacturerService.getById(dto.getManufacturer())));
//        entity = repo.save(entity);
//        return Mapper.toLockFullDTO(entity);
    }

    public Mono<LockFullDTO> deleteById(UUID id) {
        return Mono.just(repo.findById(id).orElseThrow(ResourceNotFoundException::new))
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
