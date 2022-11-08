package ru.itmo.highload.storroom.locks.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

    public Page<LockFullDTO> getAll(Pageable pageable) {
        return repo.findAll(pageable).map(Mapper::toLockFullDTO);
    }

    public LockFullDTO getById(UUID id) {
        return Mapper.toLockFullDTO(repo.findById(id).orElseThrow(ResourceNotFoundException::new));
    }

    public LockFullDTO create(LockDTO dto) {
        ManufacturerDTO manufacturer = manufacturerService.getById(dto.getManufacturer());
        LockEntity entity = Mapper.toLockEntity(dto);
        entity.setManufacturer(Mapper.toManufacturerEntity(manufacturer));
        return Mapper.toLockFullDTO(repo.save(entity));
    }

    public LockFullDTO update(UUID id, LockDTO dto) {
        LockEntity entity = repo.findById(id).orElseThrow(ResourceNotFoundException::new);
        entity.setName(dto.getName());
        entity.setManufacturer(Mapper.toManufacturerEntity(manufacturerService.getById(dto.getManufacturer())));
        entity = repo.save(entity);
        return Mapper.toLockFullDTO(entity);
    }

    public LockFullDTO deleteById(UUID id) {
        LockEntity entity = repo.findById(id).orElseThrow(ResourceNotFoundException::new);
        repo.delete(entity);
        return Mapper.toLockFullDTO(entity);
    }
}
