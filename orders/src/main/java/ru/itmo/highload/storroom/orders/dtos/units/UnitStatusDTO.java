package ru.itmo.highload.storroom.orders.dtos.units;

import lombok.Data;
import ru.itmo.highload.storroom.orders.models.UnitStatus;

@Data
public class UnitStatusDTO {
    public UnitStatus status;
}
