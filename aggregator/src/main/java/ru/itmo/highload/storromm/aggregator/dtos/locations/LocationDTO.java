package ru.itmo.highload.storromm.aggregator.dtos.locations;

import lombok.Data;
import ru.itmo.highload.storromm.aggregator.models.LocationType;

import java.util.List;
import java.util.UUID;

@Data
public class LocationDTO {
    public String address;
    public LocationType locationType;
    public List<UUID> ownerIds;
}