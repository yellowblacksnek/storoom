package ru.itmo.highload.storroom.orders.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itmo.highload.storroom.orders.dtos.UserDTO;

import java.util.UUID;

@FeignClient(name = "USERS-SERVICE")
public interface UserClient {
    @GetMapping(value = "/internal/users/{id}")
    UserDTO getUser(@PathVariable(value="id") UUID id);
}
