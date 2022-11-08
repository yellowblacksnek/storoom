package ru.itmo.highload.storroom.orders.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.itmo.highload.storroom.orders.models.OrderEntity;

import java.util.UUID;

public interface OrderRepository extends PagingAndSortingRepository<OrderEntity, UUID> {
    Page<OrderEntity> findAllByUserId(Pageable pageable, UUID userId);
}
