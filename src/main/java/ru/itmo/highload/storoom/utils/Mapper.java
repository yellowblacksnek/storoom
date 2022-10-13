package ru.itmo.highload.storoom.utils;

import static ru.itmo.highload.storoom.models.DTOs.*;

import org.springframework.stereotype.Component;
import ru.itmo.highload.storoom.consts.UserType;
import ru.itmo.highload.storoom.models.UserEntity;

@Component
public class Mapper {

    public static UserReadDTO toUserReadDTO(UserEntity entity) {
        UserReadDTO dto = new UserReadDTO();
        dto.setUsername(entity.getUsername());
        dto.setUserType(entity.getUserType().name());
        return dto;
    }

    public static UserEntity toUserEntity(UserFullDTO entity) {
        UserEntity user = new UserEntity();
        user.setUsername(entity.getUsername());
        user.setPassword(entity.getPassword());
        user.setUserType(UserType.valueOf(entity.getUserType()));
        return user;
    }
}
