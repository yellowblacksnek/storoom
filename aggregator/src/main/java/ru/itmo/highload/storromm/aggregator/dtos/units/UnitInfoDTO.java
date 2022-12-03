package ru.itmo.highload.storromm.aggregator.dtos.units;

import lombok.Data;
import ru.itmo.highload.storromm.aggregator.models.UnitType;

import java.util.UUID;

@Data
public class UnitInfoDTO {
    public Integer sizeX, sizeY, sizeZ;
    public UnitType unitType;
    public UUID locationId;
    public UUID lockId;
}
