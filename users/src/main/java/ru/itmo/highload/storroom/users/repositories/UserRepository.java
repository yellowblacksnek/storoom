package ru.itmo.highload.storroom.users.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.itmo.highload.storroom.users.models.UserType;
import ru.itmo.highload.storroom.users.models.UserEntity;

import java.util.UUID;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, UUID> {
    UserEntity findByUsername(String username);

    Page<UserEntity> findAllByUserType(Pageable pageable, UserType userType);
    Boolean existsByUsername(String username);

    UserEntity getOne(UUID id);
}

