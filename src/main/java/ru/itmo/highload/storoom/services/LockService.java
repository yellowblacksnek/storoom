package ru.itmo.highload.storoom.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.itmo.highload.storoom.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storoom.models.LockEntity;
import ru.itmo.highload.storoom.repositories.LockRepo;
import ru.itmo.highload.storoom.utils.Mapper;

import java.util.UUID;

import static ru.itmo.highload.storoom.models.DTOs.*;

@Service
@RequiredArgsConstructor
public class LockService {
    private final LockRepo repo;
    private final ManufacturerService manufacturerService;

    public Page<LockFullDTO> getAll(Pageable pageable) {
        return repo.findAll(pageable).map(Mapper::toLockFullDTO);
    }

    public LockEntity getEntityById(UUID id) {
        return repo.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public LockFullDTO getById(UUID id) {
        return Mapper.toLockFullDTO(getEntityById(id));
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
