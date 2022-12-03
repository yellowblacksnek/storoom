package ru.itmo.highload.storromm.aggregator.dtos.manufacturers;

import lombok.Data;

import java.util.UUID;

@Data
public class ManufacturerDTO {
    public UUID id;
    public String name;
}
