package ru.itmo.highload.storroom.orders.dtos;

import lombok.Data;
import ru.itmo.highload.storroom.orders.models.OrderStatus;

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