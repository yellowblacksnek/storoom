package ru.itmo.highload.storroom.locks.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class LockFullDTO {
    public UUID id;
    public String name;
    public ManufacturerDTO manufacturer;
}
