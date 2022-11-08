package ru.itmo.highload.storoom.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import static ru.itmo.highload.storoom.models.DTOs.LockDTO;
import static ru.itmo.highload.storoom.models.DTOs.LockFullDTO;
import ru.itmo.highload.storoom.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storoom.models.LockEntity;
import ru.itmo.highload.storoom.repositories.LockRepo;
import ru.itmo.highload.storoom.utils.Mapper;

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
        return Mapper.toLockFullDTO(repo.save(Mapper.toLockEntity(dto)));
    }

    public LockFullDTO updateManufacturer(UUID id, UUID manufacturer) {
        LockEntity entity = repo.findById(id).orElseThrow(ResourceNotFoundException::new);
        entity.setManufacturer(manufacturerService.getRef(manufacturer));
        entity = repo.save(entity);
        return Mapper.toLockFullDTO(entity);
    }

    public LockFullDTO deleteById(UUID id) {
        LockEntity entity = repo.findById(id).orElseThrow(ResourceNotFoundException::new);
        repo.delete(entity);
        return Mapper.toLockFullDTO(entity);
    }
}
