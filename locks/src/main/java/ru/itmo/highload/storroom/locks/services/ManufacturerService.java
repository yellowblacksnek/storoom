package ru.itmo.highload.storroom.locks.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.itmo.highload.storroom.locks.dtos.ManufacturerDTO;
import ru.itmo.highload.storroom.locks.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storroom.locks.models.ManufacturerEntity;
import ru.itmo.highload.storroom.locks.repositories.ManufacturerRepo;
import ru.itmo.highload.storroom.locks.utils.Mapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManufacturerService {
    private final ManufacturerRepo repo;

    public Page<ManufacturerDTO> getAll(Pageable pageable) {
        return repo.findAll(pageable).map(Mapper::toManufacturerDTO);
    }

    public @NonNull ManufacturerDTO getById(UUID id) {
        return Mapper.toManufacturerDTO(repo.findById(id).orElseThrow(ResourceNotFoundException::new));
    }

    public ManufacturerDTO create(ManufacturerDTO dto) {
        return Mapper.toManufacturerDTO(repo.save(Mapper.toManufacturerEntity(dto)));
    }

    public ManufacturerDTO updateName(UUID id, String name) {
        ManufacturerEntity entity = repo.findById(id).orElseThrow(ResourceNotFoundException::new);
        entity.setName(name);
        entity = repo.save(entity);
        return Mapper.toManufacturerDTO(entity);
    }

    public ManufacturerDTO deleteById(UUID id) {
        ManufacturerEntity entity = repo.findById(id).orElseThrow(ResourceNotFoundException::new);
        repo.delete(entity);
        return Mapper.toManufacturerDTO(entity);
    }
}
