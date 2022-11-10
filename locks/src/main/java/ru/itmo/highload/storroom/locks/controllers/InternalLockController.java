package ru.itmo.highload.storroom.locks.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.locks.dtos.LockFullDTO;
import ru.itmo.highload.storroom.locks.services.LockService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalLockController {
    private final LockService service;

    @GetMapping("/lock/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<LockFullDTO> getById(@PathVariable UUID id) {
        return service.getById(id);
    }
}
