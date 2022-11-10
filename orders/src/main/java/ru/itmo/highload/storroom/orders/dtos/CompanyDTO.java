package ru.itmo.highload.storroom.orders.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class CompanyDTO {
    public UUID id;
    public String name;
}
