package ru.itmo.highload.storromm.aggregator.dtos.orders;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrderInfoDTO {
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public LocalDateTime finishedTime;
    public UUID unitId;
    public UUID userId;
}
