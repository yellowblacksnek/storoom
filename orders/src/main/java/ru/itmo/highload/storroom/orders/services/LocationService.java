package ru.itmo.highload.storroom.orders.services;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.highload.storroom.orders.clients.LocationClient;
import ru.itmo.highload.storroom.orders.dtos.LocationDTO;
import ru.itmo.highload.storroom.orders.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storroom.orders.exceptions.UnavailableException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class LocationService {
    @Autowired
    private LocationClient locationClient;

    @Autowired private HttpServletRequest request;

    public LocationDTO getLocation(UUID id) {
        LocationDTO location;
        try {
            location = locationClient.getLocation(request.getHeader("Authorization"), id);
        } catch (FeignException e) {
            if(e.status() == 409) {
                throw new ResourceNotFoundException("location", id.toString());
            } else {
                throw new UnavailableException();
            }
        }
        return location;
    }

    public LocationDTO getLocationAlways(UUID id) {
        LocationDTO location;
        try {
            location = locationClient.getLocation(request.getHeader("Authorization"), id);
        } catch (FeignException e) {
            location = new LocationDTO();
            location.setId(id);
            location.setOwners(new ArrayList<>());
        }
        return location;
    }
}
