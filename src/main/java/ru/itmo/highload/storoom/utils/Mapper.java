package ru.itmo.highload.storoom.utils;

import org.springframework.stereotype.Component;
import ru.itmo.highload.storoom.consts.UserType;
import ru.itmo.highload.storoom.models.*;

import java.util.UUID;

import static ru.itmo.highload.storoom.models.DTOs.*;
import ru.itmo.highload.storoom.models.OrderEntity;
import ru.itmo.highload.storoom.models.UserEntity;

@Component
public class Mapper {

    public static UserReadDTO toUserReadDTO(UserEntity entity) {
        UserReadDTO dto = new UserReadDTO();
        dto.setId(entity.getId().toString());
        dto.setUsername(entity.getUsername());
        dto.setUserType(entity.getUserType().name());
        return dto;
    }

    public static UserEntity toUserEntity(UserFullDTO dto) {
        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setUserType(UserType.valueOf(dto.getUserType()));
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
        dto.setSizeX(entity.getSizeX().toString());
        dto.setSizeY(entity.getSizeY().toString());
        dto.setSizeZ(entity.getSizeZ().toString());
        dto.setUnitType(entity.getUnitType().toString());
        dto.setIsAvailable(entity.getIsAvailable().toString());
        dto.setLocationId(entity.getLocation().getId().toString());
        dto.setLockId(entity.getLock().getId().toString());
        return dto;
    }

    public static UnitEntity toUnitEntity(UnitDTO dto) {

        LocationEntity location = new LocationEntity();
        location.setId(UUID.fromString(dto.getLocationId()));

        LockEntity lock = new LockEntity();
        lock.setId(UUID.fromString(dto.getLockId()));

        return new UnitEntity(
                Integer.valueOf(dto.getSizeX()),
                Integer.valueOf(dto.getSizeY()),
                Integer.valueOf(dto.getSizeZ()),
                location,
                Boolean.valueOf(dto.isAvailable),
                lock
        );
    }

    public static OrderReadDTO toOrderDTO(OrderEntity entity) {
        OrderReadDTO dto = new OrderReadDTO();
        dto.setNumber(entity.getNumber());
        dto.setDays(entity.getDays());
        dto.setUnit(entity.getUnit());
        dto.setUser(entity.getUser());
        return dto;
    }

    public static OrderEntity toOrderEntity(OrderFullDTO dto) {
        OrderEntity company = new OrderEntity();
        company.setId(dto.getId());
        company.setNumber(dto.getNumber());
        company.setDays(dto.getDays());
        company.setUnit(dto.getUnit());
        company.setUser(dto.getUser());
        return company;
    }
}
