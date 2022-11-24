package ru.itmo.highload.storroom.locations.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.locations.models.OwnerLocationEntity;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class OwnerLocationRepositoryImpl implements OwnerLocationRepository{
    private final DatabaseClient client;
    @Override
    public Flux<OwnerLocationEntity> findByLocationId(UUID locationId) {
        return client.sql("SELECT * from owners_locations where location_id = :locationId")
                .bind("locationId", locationId)
                .map(row -> new OwnerLocationEntity(row.get("owner_id", UUID.class), row.get("location_id", UUID.class)))
                .all();
    }

    @Override
    public Flux<OwnerLocationEntity> findByOwnerId(UUID ownerId) {
        return client.sql("SELECT * from owners_locations where owner_id = :ownerId")
                .bind("ownerId", ownerId)
                .map(row -> new OwnerLocationEntity(row.get("owner_id", UUID.class), row.get("location_id", UUID.class)))
                .all();
    }

    @Override
    public Mono<OwnerLocationEntity> add(UUID ownerId, UUID locationId) {
        return client.sql("insert into owners_locations(owner_id, location_id) values (:ownerId,:locationId)")
                .bind("ownerId", ownerId)
                .bind("locationId", locationId)
                .map(row -> new OwnerLocationEntity(row.get("owner_id", UUID.class), row.get("location_id", UUID.class)))
                .one();
    }

    @Override
    public Mono<OwnerLocationEntity> delete(UUID ownerId, UUID locationId) {
        return client.sql("delete from owners_locations where location_id = :locationId and owner_id = :ownerId")
                .bind("ownerId", ownerId)
                .bind("locationId", locationId)
                .map(row -> new OwnerLocationEntity(row.get("owner_id", UUID.class), row.get("location_id", UUID.class)))
                .one();
    }


}
