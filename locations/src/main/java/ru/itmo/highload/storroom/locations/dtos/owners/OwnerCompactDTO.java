package ru.itmo.highload.storroom.locations.dtos.owners;

import lombok.Data;

import java.util.UUID;

@Data
public class OwnerCompactDTO {
    public UUID id;
    public String name;
    public UUID companyId;
}
