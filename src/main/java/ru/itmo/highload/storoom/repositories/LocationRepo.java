package ru.itmo.highload.storoom.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.itmo.highload.storoom.models.LocationEntity;

import java.util.UUID;

public interface LocationRepo extends PagingAndSortingRepository<LocationEntity, UUID> {
}
