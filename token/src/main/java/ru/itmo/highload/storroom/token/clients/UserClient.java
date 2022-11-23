package ru.itmo.highload.storroom.token.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itmo.highload.storroom.token.dtos.UserDTO;

@FeignClient(name = "USERS-SERVICE")
public interface UserClient {
    @GetMapping(value = "/users")
    UserDTO getUser(@RequestParam(value="username") String username);
}
