package ru.itmo.highload.storoom.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.itmo.highload.storoom.models.LockEntity;

import java.util.UUID;

public interface LockRepo extends PagingAndSortingRepository<LockEntity, UUID> {
    Boolean existsByName(String name);
}
