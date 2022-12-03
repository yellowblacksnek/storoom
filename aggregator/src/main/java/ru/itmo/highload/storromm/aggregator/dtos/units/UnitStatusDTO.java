package ru.itmo.highload.storromm.aggregator.dtos.units;

import lombok.Data;
import ru.itmo.highload.storromm.aggregator.models.UnitStatus;

@Data
public class UnitStatusDTO {
    public UnitStatus status;
}
