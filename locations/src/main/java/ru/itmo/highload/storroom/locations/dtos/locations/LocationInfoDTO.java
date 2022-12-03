package ru.itmo.highload.storroom.locations.dtos.locations;

import lombok.Data;
import ru.itmo.highload.storroom.locations.models.LocationType;

@Data public class LocationInfoDTO {
    public String address;
    public LocationType locationType;
}
