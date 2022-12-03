package ru.itmo.highload.storromm.aggregator.dtos.owners;

import lombok.Data;
import ru.itmo.highload.storromm.aggregator.dtos.locations.LocationCompactDTO;

import java.util.List;
import java.util.UUID;

@Data
public  class OwnerReadDTO {
    public UUID id;
    public String name;
    public UUID companyId;
    public List<LocationCompactDTO> locations;
}