package ru.itmo.highload.storroom.users.dtos;

import lombok.Data;
import ru.itmo.highload.storroom.users.models.UserType;

import java.util.UUID;

@Data
public class UserReadDTO {
    public UUID id;
    public String username;
    public UserType userType;
}
