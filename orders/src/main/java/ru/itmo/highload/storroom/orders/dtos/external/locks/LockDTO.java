package ru.itmo.highload.storroom.orders.dtos.external.locks;

import lombok.Data;

import java.util.UUID;

@Data
public class LockDTO {
    public UUID id;
    public String name;
    public ManufacturerDTO manufacturer;
}
