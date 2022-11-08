package ru.itmo.highload.storoom.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.itmo.highload.storoom.models.UnitEntity;

import java.util.UUID;

public interface UnitRepo extends PagingAndSortingRepository<UnitEntity, UUID> {
    UnitEntity getOne(UUID id);
}
