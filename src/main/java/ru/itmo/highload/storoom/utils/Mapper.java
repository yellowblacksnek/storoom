package ru.itmo.highload.storoom.utils;

import org.springframework.stereotype.Component;
import ru.itmo.highload.storoom.models.*;


import static ru.itmo.highload.storoom.models.DTOs.*;
import ru.itmo.highload.storoom.models.OrderEntity;
import ru.itmo.highload.storoom.models.UserEntity;

@Component
public class Mapper {

    public static UserReadDTO toUserReadDTO(UserEntity entity) {
        UserReadDTO dto = new UserReadDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setUserType(entity.getUserType());
        return dto;
    }

    public static UserEntity toUserEntity(UserFullDTO dto) {
        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setUserType(dto.getUserType());
        return user;
    }

    public static CompanyReadDTO toCompanyDTO(CompanyEntity entity) {
        CompanyReadDTO dto = new CompanyReadDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public static CompanyEntity toCompanyEntity(CompanyDTO dto) {
        CompanyEntity company = new CompanyEntity();
        company.setName(dto.getName());
        return company;
    }

    public static UnitDTO toUnitDTO(UnitEntity entity) {
        UnitDTO dto = new UnitDTO();
        dto.setId(entity.getId());
        dto.setSizeX(entity.getSizeX());
        dto.setSizeY(entity.getSizeY());
        dto.setSizeZ(entity.getSizeZ());
        dto.setUnitType(entity.getUnitType());
        dto.setStatus(entity.getStatus());
        dto.setLocationId(entity.getLocation().getId());
        dto.setLockId(entity.getLock().getId());
        return dto;
    }

    public static UnitEntity toUnitEntity(UnitDTO dto) {

        LocationEntity location = new LocationEntity();
        location.setId(dto.getLocationId());

        LockEntity lock = new LockEntity();
        lock.setId(dto.getLockId());

        UnitEntity entity = new UnitEntity(
                dto.getSizeX(),
                dto.getSizeY(),
                dto.getSizeZ(),
                location,
                dto.status,
                lock
        );
        entity.setId(dto.getId());
        return entity;
    }

    public static OrderDTO toOrderDTO(OrderEntity entity) {
        OrderDTO dto = new OrderDTO();
        dto.setId(entity.getId());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setFinishedTime(entity.getFinishedTime());
        dto.setStatus(entity.getStatus());
        dto.setUnitId(entity.getUnit().getId());
        dto.setUserId(entity.getUser().getId());
        return dto;
    }

    public static OrderEntity toOrderEntity(OrderDTO dto) {
        OrderEntity entity = new OrderEntity();
        entity.setId(dto.getId());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setFinishedTime(dto.getFinishedTime());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    public static LockDTO toLockDTO(LockEntity entity) {
        LockDTO dto = new LockDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setManufacturer(entity.getManufacturer().getId());
        return dto;
    }

    public static LockFullDTO toLockFullDTO(LockEntity entity) {
        LockFullDTO dto = new LockFullDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setManufacturer(toManufacturerDTO(entity.getManufacturer()));
        return dto;
    }

    public static LockEntity toLockEntity(LockDTO dto) {
        LockEntity entity = new LockEntity();
        ManufacturerEntity manufacturer = new ManufacturerEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        manufacturer.setId(dto.getManufacturer());
        entity.setManufacturer(manufacturer);
        return entity;
    }

    public static ManufacturerDTO toManufacturerDTO(ManufacturerEntity entity) {
        ManufacturerDTO dto = new ManufacturerDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public static ManufacturerEntity toManufacturerEntity(ManufacturerDTO dto) {
        ManufacturerEntity entity = new ManufacturerEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }
}
