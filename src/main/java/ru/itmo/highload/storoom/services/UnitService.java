package ru.itmo.highload.storoom.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.itmo.highload.storoom.consts.UnitStatus;
import ru.itmo.highload.storoom.consts.UnitType;
import ru.itmo.highload.storoom.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storoom.models.DTOs.UnitDTO;
import ru.itmo.highload.storoom.models.DTOs.UnitFullDTO;
import ru.itmo.highload.storoom.models.LocationEntity;
import ru.itmo.highload.storoom.models.LockEntity;
import ru.itmo.highload.storoom.models.UnitEntity;
import ru.itmo.highload.storoom.repositories.UnitRepo;
import ru.itmo.highload.storoom.utils.Mapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnitService {

    private final UnitRepo repo;
    private final LocationService locationService;
    private final LockService lockService;

    public UnitEntity getEntityById(UUID id) {
        return repo.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public Page<UnitFullDTO> getAll(Pageable pageable) {
        Page<UnitEntity> res = repo.findAll(pageable);
        return res.map(Mapper::toUnitFullDTO);
    }

    public UnitFullDTO create(UnitDTO dto) {
        UnitEntity entity = repo.save(Mapper.toUnitEntity(dto));
        return Mapper.toUnitFullDTO(entity);
    }

    public UnitFullDTO updateInfo(UUID id, UnitDTO dto) {
//        UnitEntity newEntity = Mapper.toUnitEntity(dto);
        UnitEntity entity = repo.findById(id).orElseThrow(ResourceNotFoundException::new);

        if (entity.getStatus() != dto.getStatus()) {
            throw new IllegalArgumentException("status updates via info updates not supported");
        }

        LocationEntity location = locationService.getEntityById(dto.getLocationId());
        LockEntity lockEntity = lockService.getEntityById(dto.getLockId());

        entity.setSizeX(dto.getSizeX());
        entity.setSizeY(dto.getSizeY());
        entity.setSizeZ(dto.getSizeZ());
        entity.setLocation(location);
        entity.setLock(lockEntity);

        UnitType oldType = entity.getUnitType();
        entity.updateUnitType();
        if (oldType != dto.getUnitType() && entity.getUnitType() != dto.getUnitType()) {
            throw new IllegalArgumentException("target unit type doesn't match with computed");
        }

        entity = repo.save(entity);
        return Mapper.toUnitFullDTO(entity);
    }

    public UnitFullDTO updateStatus(UUID id, UnitStatus status) {
        UnitEntity entity = repo.findById(id).orElseThrow(ResourceNotFoundException::new);
        entity.setStatus(status);
        entity = repo.save(entity);
        return Mapper.toUnitFullDTO(entity);
    }

    public UnitFullDTO delete(UUID id) {
        UnitEntity entity = repo.findById(id).orElse(null);
        if (entity == null) throw new ResourceNotFoundException("unit not found");
        repo.delete(entity);
        return Mapper.toUnitFullDTO(entity);
    }
}
