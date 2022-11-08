package ru.itmo.highload.storoom.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.itmo.highload.storoom.models.ManufacturerEntity;

import java.util.UUID;

public interface ManufacturerRepo extends PagingAndSortingRepository<ManufacturerEntity, UUID> {
    ManufacturerEntity getOne(UUID id);
}
