package ru.itmo.highload.storromm.aggregator.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storromm.aggregator.annotations.CustomizedOperation;
import ru.itmo.highload.storromm.aggregator.clients.LockClient;
import ru.itmo.highload.storromm.aggregator.dtos.locks.LockDTO;
import ru.itmo.highload.storromm.aggregator.dtos.locks.LockFullDTO;
import ru.itmo.highload.storromm.aggregator.dtos.locks.LockInfoDTO;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/locks")
@RequiredArgsConstructor
@Tag(name = "Locks")
public class LockController {
    private final LockClient lockClient;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    @CustomizedOperation(description = "Get lock by id.", responseCodes = {400, 401, 403, 404})
    public Mono<LockFullDTO> getLock(@PathVariable UUID id) {
        return lockClient.getLockById(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('superuser')")
    @CustomizedOperation(description = "Get all locks.", pageable = true, responseCodes = {401, 403})
    public Mono<Page<LockFullDTO>> getLocks(Pageable pageable) {
        return lockClient.getAllLocks(pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('superuser')")
    @CustomizedOperation(description = "Add lock.", responseCodes = {400, 401, 403})
    public Mono<LockFullDTO> createLock(@RequestBody LockDTO dto) {
        return lockClient.createLock(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    @CustomizedOperation(description = "Update lock's info.", responseCodes = {400, 401, 403, 404, 409})
    public Mono<LockFullDTO> updateLock(@PathVariable UUID id, @RequestBody LockInfoDTO dto) {
        return lockClient.updateLock(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    @CustomizedOperation(description = "Delete lock.", responseCodes = {400, 401, 403, 404, 409})
    public Mono<LockFullDTO> deleteLockById(@PathVariable UUID id) {
        return lockClient.deleteLock(id);
    }
}
