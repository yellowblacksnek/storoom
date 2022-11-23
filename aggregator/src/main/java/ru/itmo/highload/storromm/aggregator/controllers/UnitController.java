package ru.itmo.highload.storromm.aggregator.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storromm.aggregator.clients.OrderClient;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/units")
@RequiredArgsConstructor
public class UnitController {
    private final OrderClient unitClient;

    @GetMapping
    public ResponseEntity<Object> getAll( Pageable pageable) {
        return unitClient.getAllUnits(pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<Object> create(@RequestBody Map<String, String> dto) {
        return unitClient.createUnit(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<Object> updateInfo(@PathVariable UUID id, @RequestBody Map<String, String> dto) {
        return unitClient.updateUnitInfo(id, dto);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<Object> updateStatus(@PathVariable UUID id, @RequestBody Map<String, String> dto) {
        return unitClient.updateUnitStatus(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {
        return unitClient.deleteUnit(id);
    }
}
