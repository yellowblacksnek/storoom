package ru.itmo.highload.storoom.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.itmo.highload.storoom.models.UserEntity;

import java.util.UUID;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, UUID> {
    UserEntity findByUsername(String username);
    Boolean existsByUsername(String username);
}
