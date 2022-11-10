package ru.itmo.highload.storoom.repositories;

import lombok.NonNull;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.itmo.highload.storoom.models.LocationEntity;
import ru.itmo.highload.storoom.models.UnitEntity;

import java.util.List;
import java.util.UUID;

public interface UnitRepo extends PagingAndSortingRepository<UnitEntity, UUID> {
    UnitEntity getOne(UUID id);
    List<UnitEntity> getAllByLocation(@NonNull LocationEntity location);
}
