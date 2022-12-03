package ru.itmo.highload.storroom.orders.controllers;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storroom.orders.dtos.units.UnitDTO;
import ru.itmo.highload.storroom.orders.dtos.units.UnitFullDTO;
import ru.itmo.highload.storroom.orders.dtos.units.UnitInfoDTO;
import ru.itmo.highload.storroom.orders.dtos.units.UnitStatusDTO;
import ru.itmo.highload.storroom.orders.services.UnitService;

import java.util.UUID;


@RestController
@RequestMapping("/units")
public class UnitController {

    @Autowired
    private UnitService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<UnitDTO> getAll(@ParameterObject Pageable pageable) {
        return service.getAll(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UnitFullDTO create(@RequestBody UnitDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UnitFullDTO updateInfo(@PathVariable UUID id, @RequestBody UnitInfoDTO dto) {
        return service.updateInfo(id, dto);
    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public UnitFullDTO updateStatus(@PathVariable UUID id, @RequestBody UnitStatusDTO dto) {
        return service.updateStatus(id, dto.getStatus());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UnitFullDTO delete(@PathVariable UUID id) {
        return service.delete(id);
    }
}
