package ru.itmo.highload.storromm.aggregator.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storromm.aggregator.clients.LockClient;
import ru.itmo.highload.storromm.aggregator.dtos.locks.LockDTO;
import ru.itmo.highload.storromm.aggregator.dtos.locks.LockFullDTO;
import ru.itmo.highload.storromm.aggregator.dtos.locks.LockInfoDTO;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/locks")
@RequiredArgsConstructor
public class LockController {
    private final LockClient lockClient;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    public Mono<LockFullDTO> getById(@PathVariable UUID id) {
        return lockClient.getLockById(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('superuser')")
    public Mono<Page<LockFullDTO>> getAll(Pageable pageable) {
        return lockClient.getAllLocks(pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('superuser')")
    public Mono<LockFullDTO> create(@RequestBody LockDTO dto) {
        return lockClient.createLock(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    public Mono<LockFullDTO> update(@PathVariable UUID id, @RequestBody LockInfoDTO dto) {
        return lockClient.updateLock(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    public Mono<LockFullDTO> deleteById(@PathVariable UUID id) {
        return lockClient.deleteLock(id);
    }
}
