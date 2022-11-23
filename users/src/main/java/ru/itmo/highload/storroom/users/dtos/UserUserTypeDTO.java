package ru.itmo.highload.storroom.users.dtos;

import lombok.Data;
import ru.itmo.highload.storroom.users.models.UserType;

@Data
public class UserUserTypeDTO {
    UserType userType;
}
