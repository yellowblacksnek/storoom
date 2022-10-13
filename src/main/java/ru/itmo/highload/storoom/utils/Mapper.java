package ru.itmo.highload.storoom.utils;

import org.springframework.stereotype.Component;
import ru.itmo.highload.storoom.consts.UserType;
import ru.itmo.highload.storoom.models.CompanyEntity;
import ru.itmo.highload.storoom.models.UserEntity;

import static ru.itmo.highload.storoom.models.DTOs.*;

@Component
public class Mapper {

    public static UserReadDTO toUserReadDTO(UserEntity entity) {
        UserReadDTO dto = new UserReadDTO();
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
}
