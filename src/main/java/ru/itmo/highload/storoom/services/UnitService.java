package ru.itmo.highload.storoom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.itmo.highload.storoom.consts.UnitStatus;
import ru.itmo.highload.storoom.consts.UnitType;
import ru.itmo.highload.storoom.exceptions.BadRequestException;
import ru.itmo.highload.storoom.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storoom.models.DTOs.UnitDTO;
import ru.itmo.highload.storoom.models.UnitEntity;
import ru.itmo.highload.storoom.repositories.UnitRepo;
import ru.itmo.highload.storoom.utils.Mapper;

import java.util.UUID;

@Service
public class UnitService {

    @Autowired
    private UnitRepo repo;

    public UnitEntity getRef(UUID id) {
        return repo.getOne(id);
    }

    public UnitEntity getById(UUID id) {
        return repo.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public Page<UnitDTO> getAll(Pageable pageable) {
        Page<UnitEntity> res = repo.findAll(pageable);
        return res.map(Mapper::toUnitDTO);
    }

    public UnitDTO create(UnitDTO dto) {
        UnitEntity entity = repo.save(Mapper.toUnitEntity(dto));
        return Mapper.toUnitDTO(entity);
    }

    public UnitDTO updateInfo(UUID id, UnitDTO dto) {
        UnitEntity newEntity = Mapper.toUnitEntity(dto);
        UnitEntity entity = repo.findById(id).orElseThrow(ResourceNotFoundException::new);

        if(entity.getStatus() != newEntity.getStatus()) {
            throw new BadRequestException("status updates via info updates not supported");
        }

        entity.setSizeX(newEntity.getSizeX());
        entity.setSizeY(newEntity.getSizeY());
        entity.setSizeZ(newEntity.getSizeZ());
        entity.setLocation(newEntity.getLocation());
        entity.setLock(newEntity.getLock());

        UnitType oldType = entity.getUnitType();
        entity.updateUnitType();
        if(oldType != newEntity.getUnitType() && entity.getUnitType() != newEntity.getUnitType()) {
            throw new BadRequestException("target unit type doesn't match with computed");
        }

        entity = repo.save(entity);
        return Mapper.toUnitDTO(entity);
    }

    public UnitDTO updateStatus(UUID id, UnitStatus status) {
        UnitEntity entity = repo.findById(id).orElseThrow(ResourceNotFoundException::new);
        entity.setStatus(status);
        entity = repo.save(entity);
        return Mapper.toUnitDTO(entity);
    }

    public UnitDTO delete(UUID id) {
        UnitEntity entity = repo.findById(id).orElse(null);
        if(entity == null) throw new ResourceNotFoundException("unit not found");
        repo.delete(entity);
        return Mapper.toUnitDTO(entity);
    }

}
