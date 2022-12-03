package ru.itmo.highload.storroom.locations.dtos.owners;

import lombok.Data;
import ru.itmo.highload.storroom.locations.dtos.locations.LocationCompactDTO;

import java.util.List;
import java.util.UUID;

@Data
public  class OwnerReadDTO {
    public UUID id;
    public String name;
    public UUID companyId;
    public List<LocationCompactDTO> locations;
}