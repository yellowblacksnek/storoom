package ru.itmo.highload.storroom.orders.dtos.units;

import lombok.Data;
import ru.itmo.highload.storroom.orders.dtos.external.locations.LocationDTO;
import ru.itmo.highload.storroom.orders.dtos.external.locks.LockDTO;
import ru.itmo.highload.storroom.orders.models.UnitStatus;
import ru.itmo.highload.storroom.orders.models.UnitType;

import java.util.UUID;

@Data
public class UnitFullDTO {
    public UUID id;
    public Integer sizeX, sizeY, sizeZ;
    public UnitType unitType;
    public UnitStatus status;
    public LocationDTO location;
    public LockDTO lock;
}
