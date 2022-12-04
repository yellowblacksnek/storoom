package ru.itmo.highload.storroom.locations.dtos.owners;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OwnerDTO {
    public String name;
    public UUID companyId;
    public List<UUID> locationIds;
}