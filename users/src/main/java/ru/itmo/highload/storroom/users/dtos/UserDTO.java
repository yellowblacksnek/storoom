package ru.itmo.highload.storroom.users.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {
    public UUID id;
    public String username;
}
