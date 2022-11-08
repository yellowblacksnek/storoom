package ru.itmo.highload.storroom.orders.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.highload.storroom.orders.models.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    public UUID id;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public LocalDateTime finishedTime;
    public OrderStatus status;
    public UUID unitId;
    public UUID userId;
}
