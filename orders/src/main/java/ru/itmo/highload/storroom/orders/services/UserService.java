package ru.itmo.highload.storroom.orders.services;

import java.util.UUID;

public interface UserService {
    public Boolean canViewOrders(UUID userId, String username);
}
