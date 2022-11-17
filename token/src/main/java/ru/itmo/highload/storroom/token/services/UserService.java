package ru.itmo.highload.storroom.token.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.highload.storroom.token.clients.UserClient;
import ru.itmo.highload.storroom.token.dtos.UserDTO;
import ru.itmo.highload.storroom.token.utils.JwtHelper;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserClient client;
    private final JwtHelper jwt;

    public UserDTO getUser(String username) {
        String token = "Bearer " + jwt.generateServiceToken();
        return client.getUser(token, username);
    }
}
