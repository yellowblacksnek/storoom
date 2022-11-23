package ru.itmo.highload.storroom.token.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itmo.highload.storroom.token.dtos.UserDTO;

@FeignClient("USERS-SERVICE")
public interface UserClient {
    @GetMapping(value = "/internal/user")
    UserDTO getUser(@RequestHeader(value = "Authorization") String authorizationHeader,
                    @RequestParam(value="username") String username);
}
