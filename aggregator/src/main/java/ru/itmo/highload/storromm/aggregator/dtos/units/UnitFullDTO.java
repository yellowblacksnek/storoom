package ru.itmo.highload.storromm.aggregator.dtos.units;

import lombok.Data;
import ru.itmo.highload.storromm.aggregator.dtos.locations.LocationDTO;
import ru.itmo.highload.storromm.aggregator.dtos.locks.LockDTO;
import ru.itmo.highload.storromm.aggregator.models.UnitStatus;
import ru.itmo.highload.storromm.aggregator.models.UnitType;

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
