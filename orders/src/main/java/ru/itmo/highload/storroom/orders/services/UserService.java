package ru.itmo.highload.storroom.orders.services;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itmo.highload.storroom.orders.clients.UserClient;
import ru.itmo.highload.storroom.orders.dtos.UserDTO;
import ru.itmo.highload.storroom.orders.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storroom.orders.exceptions.UnavailableException;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserClient client;
    public Boolean canViewOrders(UUID userId, String username) {
        UserDTO user = client.getUser(userId);
        return user.getUsername().equals(username);
    }

    public UserDTO getUser(UUID id) {
        UserDTO user;
        try {
            user = client.getUser(id);
        } catch (FeignException e) {
            if(e.status() == 409) {
                throw new ResourceNotFoundException("user", id.toString());
            } else {
                throw new UnavailableException();
            }
        }
        return user;
    }

    public UserDTO getUserAlways(UUID id) {
        UserDTO user;
        try {
            user = client.getUser(id);
        } catch (FeignException e) {
            user = new UserDTO();
            user.setId(id);

        }
        return user;
    }
}
