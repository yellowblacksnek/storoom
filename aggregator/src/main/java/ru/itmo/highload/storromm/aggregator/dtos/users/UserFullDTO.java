package ru.itmo.highload.storromm.aggregator.dtos.users;

import lombok.Data;
import ru.itmo.highload.storromm.aggregator.models.UserType;

import java.util.UUID;

@Data
public class UserFullDTO {
    public UUID id;
    public String username;
    public String password;
    public UserType userType;
}
