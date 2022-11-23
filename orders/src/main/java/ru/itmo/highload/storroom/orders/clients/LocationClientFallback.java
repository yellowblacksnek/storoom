package ru.itmo.highload.storroom.orders.clients;

import org.springframework.stereotype.Component;
import ru.itmo.highload.storroom.orders.dtos.LocationDTO;

import java.util.ArrayList;
import java.util.UUID;

@Component
public class LocationClientFallback implements LocationClient{
    @Override
    public LocationDTO getLocation(UUID id) {
        LocationDTO location = new LocationDTO();
        location.setId(id);
        location.setOwners(new ArrayList<>());
        return location;
    }
}
