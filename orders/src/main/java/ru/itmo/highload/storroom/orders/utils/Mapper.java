package ru.itmo.highload.storroom.orders.utils;

import ru.itmo.highload.storroom.orders.dtos.*;
import ru.itmo.highload.storroom.orders.models.OrderEntity;
import ru.itmo.highload.storroom.orders.models.UnitEntity;

public class Mapper {
    public static UnitDTO toUnitDTO(UnitEntity entity) {
        UnitDTO dto = new UnitDTO();
        dto.setId(entity.getId());
        dto.setSizeX(entity.getSizeX());
        dto.setSizeY(entity.getSizeY());
        dto.setSizeZ(entity.getSizeZ());
        dto.setUnitType(entity.getUnitType());
        dto.setStatus(entity.getStatus());
        dto.setLocationId(entity.getLocationId());
        dto.setLockId(entity.getLockId());
        return dto;
    }

    public static UnitFullDTO toUnitFullDTO(UnitEntity entity, LockDTO lock) {
        UnitFullDTO dto = new UnitFullDTO();
        dto.setId(entity.getId());
        dto.setSizeX(entity.getSizeX());
        dto.setSizeY(entity.getSizeY());
        dto.setSizeZ(entity.getSizeZ());
        dto.setUnitType(entity.getUnitType());
        dto.setStatus(entity.getStatus());
        dto.setLocationId(entity.getLocationId());
        dto.setLock(lock);
        return dto;
    }

    public static UnitEntity toUnitEntity(UnitDTO dto) {
        UnitEntity entity = new UnitEntity(
                dto.getSizeX(),
                dto.getSizeY(),
                dto.getSizeZ(),
                dto.getLocationId(),
                dto.status,
                dto.getLockId()
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
        dto.setUserId(entity.getUserId());
        return dto;
    }

    public static OrderFullDTO toOrderFullDTO(OrderEntity entity, UserDTO user, LockDTO lock) {
        OrderFullDTO dto = new OrderFullDTO();
        dto.setId(entity.getId());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setFinishedTime(entity.getFinishedTime());
        dto.setStatus(entity.getStatus());
        dto.setUnit(toUnitFullDTO(entity.getUnit(), lock));
        dto.setUser(user);
        return dto;
    }

    public static OrderEntity toOrderEntity(OrderDTO dto) {
        OrderEntity entity = new OrderEntity();
        entity.setId(dto.getId());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setFinishedTime(dto.getFinishedTime());
        entity.setStatus(dto.getStatus());
        entity.setUserId(dto.getUserId());
        return entity;
    }
}
