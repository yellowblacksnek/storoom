package ru.itmo.highload.storromm.aggregator.dtos.locations;

import lombok.Data;
import ru.itmo.highload.storromm.aggregator.models.LocationType;

import java.util.UUID;

@Data
public class LocationCompactDTO {
    public UUID id;
    public String address;
    public LocationType locationType;
}
