package ru.itmo.highload.storroom.orders.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.itmo.highload.storroom.orders.dtos.UnitDTO;
import ru.itmo.highload.storroom.orders.exceptions.BadRequestException;
import ru.itmo.highload.storroom.orders.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storroom.orders.models.UnitEntity;
import ru.itmo.highload.storroom.orders.models.UnitStatus;
import ru.itmo.highload.storroom.orders.models.UnitType;
import ru.itmo.highload.storroom.orders.repositories.UnitRepo;
import ru.itmo.highload.storroom.orders.utils.Mapper;

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
        entity.setLocationId(newEntity.getLocationId());
        entity.setLockId(newEntity.getLockId());

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
