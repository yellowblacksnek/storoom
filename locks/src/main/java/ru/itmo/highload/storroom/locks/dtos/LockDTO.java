package ru.itmo.highload.storroom.locks.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class LockDTO {
    public UUID id;
    public String name;
    public UUID manufacturer;
}
