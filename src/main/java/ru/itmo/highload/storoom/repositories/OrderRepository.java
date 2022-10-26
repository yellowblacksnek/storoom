package ru.itmo.highload.storoom.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.itmo.highload.storoom.models.OrderEntity;
import ru.itmo.highload.storoom.models.UserEntity;

import java.util.UUID;

public interface OrderRepository extends PagingAndSortingRepository<OrderEntity, UUID> {
    OrderEntity findByName(String name);
    Page<OrderEntity> findAllByUser(Pageable pageable, String userName);
    Boolean existsByNumber(Integer number);
}
