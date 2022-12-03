package ru.itmo.highload.storroom.orders.dtos.external.locations;

import lombok.Data;

import java.util.UUID;

@Data
public class CompanyDTO {
    public UUID id;
    public String name;
}
