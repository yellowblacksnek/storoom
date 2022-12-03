package ru.itmo.highload.storroom.orders.dtos.external.users;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {
    public UUID id;
    public String username;
}
