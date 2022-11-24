package ru.itmo.highload.storroom.locations.dtos;

import lombok.Data;
import ru.itmo.highload.storroom.locations.models.LocationType;

import java.util.List;
import java.util.UUID;

@Data
public class LocationReadDTO {
    public UUID id;
    public String address;
    public LocationType locationType;
    public List<OwnerCompactDTO> owners;
}