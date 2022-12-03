package ru.itmo.highload.storromm.aggregator.dtos.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.highload.storromm.aggregator.models.OrderStatus;

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
