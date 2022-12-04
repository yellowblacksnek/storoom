package ru.itmo.highload.storromm.aggregator.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storromm.aggregator.clients.OrderClient;
import ru.itmo.highload.storromm.aggregator.dtos.units.UnitDTO;
import ru.itmo.highload.storromm.aggregator.dtos.units.UnitFullDTO;
import ru.itmo.highload.storromm.aggregator.dtos.units.UnitInfoDTO;
import ru.itmo.highload.storromm.aggregator.dtos.units.UnitStatusDTO;

import java.util.UUID;

@RestController
@RequestMapping("/units")
@RequiredArgsConstructor
@Tag(name = "Units")
public class UnitController {
    private final OrderClient unitClient;

    @GetMapping
    public ResponseEntity<Page<UnitDTO>> getAll(Pageable pageable) {
        return unitClient.getAllUnits(pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<UnitFullDTO> create(@RequestBody UnitDTO dto) {
        return unitClient.createUnit(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<UnitFullDTO> updateInfo(@PathVariable UUID id, @RequestBody UnitInfoDTO dto) {
        return unitClient.updateUnitInfo(id, dto);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<UnitFullDTO> updateStatus(@PathVariable UUID id, @RequestBody UnitStatusDTO dto) {
        return unitClient.updateUnitStatus(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<UnitFullDTO> delete(@PathVariable UUID id) {
        return unitClient.deleteUnit(id);
    }
}
