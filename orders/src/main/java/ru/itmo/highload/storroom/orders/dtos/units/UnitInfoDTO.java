package ru.itmo.highload.storroom.orders.dtos.units;

import lombok.Data;
import ru.itmo.highload.storroom.orders.models.UnitType;

import java.util.UUID;

@Data
public class UnitInfoDTO {
    public Integer sizeX, sizeY, sizeZ;
    public UnitType unitType;
    public UUID locationId;
    public UUID lockId;
}
