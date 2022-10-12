package ru.itmo.highload.storoom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.highload.storoom.models.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
