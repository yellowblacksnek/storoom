package ru.itmo.highload.storromm.aggregator.clients;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storromm.aggregator.dtos.locks.LockDTO;
import ru.itmo.highload.storromm.aggregator.dtos.locks.LockFullDTO;
import ru.itmo.highload.storromm.aggregator.dtos.locks.LockInfoDTO;
import ru.itmo.highload.storromm.aggregator.dtos.manufacturers.ManufacturerDTO;
import ru.itmo.highload.storromm.aggregator.dtos.manufacturers.ManufacturerNameDTO;

import java.util.UUID;

@ReactiveFeignClient(name = "LOCKS-SERVICE")
public interface LockClient {
    @GetMapping(value = "/locks/{id}")
    Mono<LockFullDTO> getLockById(@PathVariable(value="id") UUID id);

    @GetMapping(value = "/locks")
    Mono<Page<LockFullDTO>> getAllLocks(Pageable pageable);

    @PostMapping(value = "/locks")
    Mono<LockFullDTO> createLock(@RequestBody LockDTO body);

    @PutMapping(value = "/locks/{id}")
    Mono<LockFullDTO> updateLock(@PathVariable UUID id, @RequestBody LockInfoDTO body);

    @DeleteMapping(value = "/locks/{id}")
    Mono<LockFullDTO> deleteLock(@PathVariable UUID id);

    @GetMapping(value = "/manufacturers/{id}")
    Mono<ManufacturerDTO> getManufacturerById(@PathVariable(value="id") UUID id);

    @GetMapping(value = "/manufacturers")
    Mono<ManufacturerDTO> getAllManufacturers(Pageable pageable);

    @PostMapping(value = "/manufacturers")
    Mono<ManufacturerDTO> createManufacturer(@RequestBody ManufacturerDTO body);

    @PutMapping(value = "/manufacturers/{id}/name")
    Mono<ManufacturerDTO> updateManufacturerName(@PathVariable UUID id, @RequestBody ManufacturerNameDTO body);

    @DeleteMapping(value = "/manufacturers/{id}")
    Mono<ManufacturerDTO> deleteManufacturer(@PathVariable UUID id);
}