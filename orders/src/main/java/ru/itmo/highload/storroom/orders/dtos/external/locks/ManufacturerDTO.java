package ru.itmo.highload.storroom.orders.dtos.external.locks;

import lombok.Data;

import java.util.UUID;

@Data
public class ManufacturerDTO {
    public UUID id;
    public String name;
}
