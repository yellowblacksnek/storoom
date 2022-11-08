package ru.itmo.highload.storroom.orders.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.itmo.highload.storroom.orders.models.UnitEntity;

import java.util.UUID;

public interface UnitRepo extends PagingAndSortingRepository<UnitEntity, UUID> {
    UnitEntity getOne(UUID id);
}
