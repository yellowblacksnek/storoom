package ru.itmo.highload.storroom.users.utils;

import ru.itmo.highload.storroom.users.dtos.UserFullDTO;
import ru.itmo.highload.storroom.users.dtos.UserReadDTO;
import ru.itmo.highload.storroom.users.models.UserEntity;

public class Mapper {
    public static UserReadDTO toUserReadDTO(UserEntity entity) {
        UserReadDTO dto = new UserReadDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setUserType(entity.getUserType());
        return dto;
    }

    public static UserFullDTO toUserFullDTO(UserEntity entity) {
        UserFullDTO dto = new UserFullDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setPassword(entity.getPassword());
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
}
