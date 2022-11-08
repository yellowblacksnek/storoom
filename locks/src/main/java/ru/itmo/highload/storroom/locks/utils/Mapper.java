package ru.itmo.highload.storroom.locks.utils;

import ru.itmo.highload.storroom.locks.dtos.LockDTO;
import ru.itmo.highload.storroom.locks.dtos.LockFullDTO;
import ru.itmo.highload.storroom.locks.dtos.ManufacturerDTO;
import ru.itmo.highload.storroom.locks.models.LockEntity;
import ru.itmo.highload.storroom.locks.models.ManufacturerEntity;

public class Mapper {
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
