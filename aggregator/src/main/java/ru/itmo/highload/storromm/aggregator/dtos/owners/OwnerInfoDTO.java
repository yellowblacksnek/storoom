package ru.itmo.highload.storromm.aggregator.dtos.owners;

import lombok.Data;

import java.util.UUID;

@Data public class OwnerInfoDTO {
    public String name;
    public UUID companyId;
}
