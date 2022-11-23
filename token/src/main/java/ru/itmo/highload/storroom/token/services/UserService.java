package ru.itmo.highload.storroom.token.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.highload.storroom.token.clients.UserClient;
import ru.itmo.highload.storroom.token.dtos.UserDTO;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserClient client;

    public UserDTO getUser(String username) {
        return client.getUser(username);
    }
}
