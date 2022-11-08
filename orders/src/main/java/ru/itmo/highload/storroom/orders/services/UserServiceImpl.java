package ru.itmo.highload.storroom.orders.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itmo.highload.storroom.orders.clients.UserClient;
import ru.itmo.highload.storroom.orders.dtos.UserDTO;

import java.util.UUID;

@Component(value="userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserClient client;
    @Override
    public Boolean canViewOrders(UUID userId, String username) {
        UserDTO user = client.getUser(userId);
        return user.getUsername().equals(username);
    }
}
