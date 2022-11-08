package ru.itmo.highload.storroom.locks.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.itmo.highload.storroom.locks.models.LockEntity;

import java.util.UUID;

public interface LockRepo extends PagingAndSortingRepository<LockEntity, UUID> {
}
