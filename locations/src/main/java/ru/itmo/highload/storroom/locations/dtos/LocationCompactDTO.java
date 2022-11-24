package ru.itmo.highload.storroom.locations.dtos;

import lombok.Data;
import ru.itmo.highload.storroom.locations.models.LocationType;

import java.util.UUID;

@Data
public class LocationCompactDTO {
    public UUID id;
    public String address;
    public LocationType locationType;
}
