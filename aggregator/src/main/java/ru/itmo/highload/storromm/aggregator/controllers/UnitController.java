package ru.itmo.highload.storromm.aggregator.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storromm.aggregator.annotations.CustomizedOperation;
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
    @CustomizedOperation(description = "Get all units.", pageable = true, responseCodes = {401, 403})
    public ResponseEntity<Page<UnitDTO>> getUnits(Pageable pageable) {
        return unitClient.getAllUnits(pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('superuser')")
    @CustomizedOperation(description = "Create unit.", responseCodes = {400, 401, 403, 409})
    public ResponseEntity<UnitFullDTO> createUnit(@RequestBody UnitDTO dto) {
        return unitClient.createUnit(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    @CustomizedOperation(description = "Update unit info.", responseCodes = {400, 401, 403, 404, 409})
    public ResponseEntity<UnitFullDTO> updateUnitInfo(@PathVariable UUID id, @RequestBody UnitInfoDTO dto) {
        return unitClient.updateUnitInfo(id, dto);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('superuser')")
    @CustomizedOperation(description = "Update unit status.", responseCodes = {400, 401, 403, 404, 409})
    public ResponseEntity<UnitFullDTO> updateUnitStatus(@PathVariable UUID id, @RequestBody UnitStatusDTO dto) {
        return unitClient.updateUnitStatus(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    @CustomizedOperation(description = "Delete unit.", responseCodes = {400, 401, 403, 404, 409})
    public ResponseEntity<UnitFullDTO> deleteUnit(@PathVariable UUID id) {
        return unitClient.deleteUnit(id);
    }
}
