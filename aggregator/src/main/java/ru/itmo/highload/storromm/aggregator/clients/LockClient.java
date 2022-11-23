package ru.itmo.highload.storromm.aggregator.clients;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@ReactiveFeignClient(name = "LOCKS-SERVICE")
public interface LockClient {
    @GetMapping(value = "/locks/{id}")
    Mono<Object> getLockById(@PathVariable(value="id") UUID id);

    @GetMapping(value = "/locks")
    Mono<Object> getAllLocks(Pageable pageable);

    @PostMapping(value = "/locks")
    Mono<Object> createLock(@RequestBody Map<String, String> body);

    @PutMapping(value = "/locks/{id}")
    Mono<Object> updateLock(@PathVariable UUID id, @RequestBody Map<String, String> body);

    @DeleteMapping(value = "/locks/{id}")
    Mono<Object> deleteLock(@PathVariable UUID id);

    @GetMapping(value = "/manufacturers/{id}")
    Mono<Object> getManufacturerById(@PathVariable(value="id") UUID id);

    @GetMapping(value = "/manufacturers")
    Mono<Object> getAllManufacturers(Pageable pageable);

    @PostMapping(value = "/manufacturers")
    Mono<Object> createManufacturer(@RequestBody Map<String, String> body);

    @PutMapping(value = "/manufacturers/{id}/name")
    Mono<Object> updateManufacturerName(@PathVariable UUID id, @RequestBody Map<String, String> body);

    @DeleteMapping(value = "/manufacturers/{id}")
    Mono<Object> deleteManufacturer(@PathVariable UUID id);
}