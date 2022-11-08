package ru.itmo.highload.storroom.locks.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.itmo.highload.storroom.locks.models.ManufacturerEntity;

import java.util.UUID;

public interface ManufacturerRepo extends PagingAndSortingRepository<ManufacturerEntity, UUID> {
    ManufacturerEntity getOne(UUID id);
}
