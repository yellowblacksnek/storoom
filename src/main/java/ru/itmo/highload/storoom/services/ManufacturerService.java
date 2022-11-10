package ru.itmo.highload.storoom.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.itmo.highload.storoom.exceptions.ResourceAlreadyExistsException;
import ru.itmo.highload.storoom.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storoom.models.ManufacturerEntity;
import ru.itmo.highload.storoom.repositories.ManufacturerRepo;
import ru.itmo.highload.storoom.utils.Mapper;

import java.util.UUID;

import static ru.itmo.highload.storoom.models.DTOs.ManufacturerDTO;

@Service
@RequiredArgsConstructor
public class ManufacturerService {
    private final ManufacturerRepo repo;

    public Page<ManufacturerDTO> getAll(Pageable pageable) {
        return repo.findAll(pageable).map(Mapper::toManufacturerDTO);
    }

    public ManufacturerDTO getById(UUID id) {
        return Mapper.toManufacturerDTO(repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Manufacturer", id)));
    }

    public ManufacturerDTO create(ManufacturerDTO dto) {
        if (repo.existsByName(dto.getName())) {
            throw new ResourceAlreadyExistsException();
        }
        return Mapper.toManufacturerDTO(repo.save(Mapper.toManufacturerEntity(dto)));
    }

    public ManufacturerDTO updateName(UUID id, String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("no name provided");
        }
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
