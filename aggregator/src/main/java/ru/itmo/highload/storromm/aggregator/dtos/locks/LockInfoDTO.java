package ru.itmo.highload.storromm.aggregator.dtos.locks;

import lombok.Data;

import java.util.UUID;

@Data public class LockInfoDTO {
    public String name;
    public UUID manufacturer;
}
