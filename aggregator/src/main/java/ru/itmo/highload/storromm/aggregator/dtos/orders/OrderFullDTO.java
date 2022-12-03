package ru.itmo.highload.storromm.aggregator.dtos.orders;

import lombok.Data;
import ru.itmo.highload.storromm.aggregator.dtos.units.UnitFullDTO;
import ru.itmo.highload.storromm.aggregator.dtos.users.UserDTO;
import ru.itmo.highload.storromm.aggregator.models.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrderFullDTO {
    public UUID id;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public LocalDateTime finishedTime;
    public OrderStatus status;
    public UnitFullDTO unit;
    public UserDTO user;
}