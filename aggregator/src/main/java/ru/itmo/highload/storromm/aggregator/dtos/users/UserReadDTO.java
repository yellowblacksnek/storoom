package ru.itmo.highload.storromm.aggregator.dtos.users;

import lombok.Data;
import ru.itmo.highload.storromm.aggregator.models.UserType;

import java.util.UUID;

@Data
public class UserReadDTO {
    public UUID id;
    public String username;
    public UserType userType;
}
