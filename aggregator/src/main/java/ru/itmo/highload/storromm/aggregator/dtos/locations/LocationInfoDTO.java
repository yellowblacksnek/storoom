package ru.itmo.highload.storromm.aggregator.dtos.locations;

import lombok.Data;
import ru.itmo.highload.storromm.aggregator.models.LocationType;

@Data public class LocationInfoDTO {
    public String address;
    public LocationType locationType;
}
