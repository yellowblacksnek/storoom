package ru.itmo.highload.storromm.aggregator.dtos.users;

import lombok.Data;
import ru.itmo.highload.storromm.aggregator.models.UserType;

@Data
public class UserUserTypeDTO {
    UserType userType;
}
