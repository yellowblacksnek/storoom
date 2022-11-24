package ru.itmo.highload.storroom.locations.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class CompanyReadDTO {
    public UUID id;
    public String name;
}
