package ru.itmo.highload.storoom.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.itmo.highload.storoom.models.OwnerEntity;

import java.util.List;
import java.util.UUID;

public interface OwnerRepo extends PagingAndSortingRepository<OwnerEntity, UUID> {
    OwnerEntity findByName(String name);

    Boolean existsByName(String name);

    List<OwnerEntity> findByIdIn(List<UUID> ids);
}
