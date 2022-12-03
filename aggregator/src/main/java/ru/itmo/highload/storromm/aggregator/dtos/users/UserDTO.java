package ru.itmo.highload.storromm.aggregator.dtos.users;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {
    public UUID id;
    public String username;
}
