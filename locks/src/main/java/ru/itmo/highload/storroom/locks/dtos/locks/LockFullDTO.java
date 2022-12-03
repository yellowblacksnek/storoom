package ru.itmo.highload.storroom.locks.dtos.locks;

import lombok.Data;
import ru.itmo.highload.storroom.locks.dtos.manufacturers.ManufacturerDTO;

import java.util.UUID;

@Data
public class LockFullDTO {
    public UUID id;
    public String name;
    public ManufacturerDTO manufacturer;
}
