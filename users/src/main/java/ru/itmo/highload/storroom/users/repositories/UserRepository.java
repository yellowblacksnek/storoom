package ru.itmo.highload.storroom.users.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.itmo.highload.storroom.users.models.UserType;
import ru.itmo.highload.storroom.users.models.UserEntity;

import java.util.List;

@Repository
public interface UserRepository {
    UserEntity findByUsername(String username);
    List<UserEntity> findAll();
    Page<UserEntity> findAll(Pageable pageable);
    Page<UserEntity> findAllByUserType(Pageable pageable, UserType userType);
    Boolean existsByUsername(String username);

    UserEntity save(UserEntity entity);
    UserEntity delete(UserEntity entity);

    int count();

}

