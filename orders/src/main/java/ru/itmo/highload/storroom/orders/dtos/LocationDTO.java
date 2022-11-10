package ru.itmo.highload.storroom.orders.dtos;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class LocationDTO {
    public UUID id;
    public String address;
    public LocationType locationType;
    public List<OwnerDTO> owners;

    enum LocationType {
        stand,
        warehouse,
        main_office
    }
}
