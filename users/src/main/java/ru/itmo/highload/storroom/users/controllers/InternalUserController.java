package ru.itmo.highload.storroom.users.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storroom.users.dtos.UserReadDTO;
import ru.itmo.highload.storroom.users.services.UserService;
import ru.itmo.highload.storroom.users.dtos.UserFullDTO;

import java.util.UUID;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalUserController {
    private final UserService service;

    @GetMapping(value = "/user", params = "username")
    @ResponseStatus(HttpStatus.OK)
    public UserFullDTO getFullUserByUsername(@RequestParam String username) {
        return service.getUserByUsername(username);
    }

    @GetMapping(value = "/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserReadDTO getReadUserById(@PathVariable UUID id){
        return service.getReadUserById(id);
    }
}
