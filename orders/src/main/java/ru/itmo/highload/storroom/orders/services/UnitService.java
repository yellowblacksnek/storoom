package ru.itmo.highload.storroom.orders.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.itmo.highload.storroom.orders.dtos.LockDTO;
import ru.itmo.highload.storroom.orders.dtos.UnitDTO;
import ru.itmo.highload.storroom.orders.dtos.UnitFullDTO;
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

    @Autowired private LockService lockService;

    public UnitEntity getById(UUID id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("unit " + id + " not found"));
    }

    public Page<UnitDTO> getAll(Pageable pageable) {
        Page<UnitEntity> res = repo.findAll(pageable);
        return res.map(Mapper::toUnitDTO);
    }

    public UnitFullDTO create(UnitDTO dto) {
        LockDTO lock = lockService.getLock(dto.getLockId());
        UnitEntity entity = repo.save(Mapper.toUnitEntity(dto));
        return Mapper.toUnitFullDTO(entity, lock);
    }

    public UnitFullDTO updateInfo(UUID id, UnitDTO dto) {
        UnitEntity entity = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("unit " + id + " not found"));

        if(entity.getStatus() != dto.getStatus()) {
            throw new IllegalArgumentException("status updates via info updates not supported");
        }
        LockDTO lock = lockService.getLock(dto.getLockId());


        entity.setSizeX(dto.getSizeX());
        entity.setSizeY(dto.getSizeY());
        entity.setSizeZ(dto.getSizeZ());
        entity.setLocationId(dto.getLocationId());
        entity.setLockId(dto.getLockId());

        UnitType oldType = entity.getUnitType();
        entity.updateUnitType();
        if(oldType != dto.getUnitType() && entity.getUnitType() != dto.getUnitType()) {
            throw new IllegalArgumentException("target unit type doesn't match with computed");
        }

        entity = repo.save(entity);
        return Mapper.toUnitFullDTO(entity, lock);
    }

    public UnitFullDTO updateStatus(UUID id, UnitStatus status) {
        UnitEntity entity = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("unit " + id + " not found"));
        entity.setStatus(status);
        entity = repo.save(entity);
        LockDTO lock = lockService.getLockAlways(entity.getLockId());
        return Mapper.toUnitFullDTO(entity, lock);
    }

    public UnitFullDTO delete(UUID id) {
        UnitEntity entity = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("unit " + id + " not found"));
        repo.delete(entity);
        LockDTO lock = lockService.getLockAlways(entity.getLockId());
        return Mapper.toUnitFullDTO(entity, lock);
    }



}
