package ru.itmo.highload.storromm.aggregator.dtos.companies;

import lombok.Data;

import java.util.UUID;

@Data
public class CompanyReadDTO {
    public UUID id;
    public String name;
}
