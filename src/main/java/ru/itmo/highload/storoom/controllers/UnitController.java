package ru.itmo.highload.storoom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storoom.models.UnitEntity;
import ru.itmo.highload.storoom.repositories.UnitRepo;
import ru.itmo.highload.storoom.utils.Mapper;

import java.util.UUID;

import static ru.itmo.highload.storoom.models.DTOs.*;

@RestController
@RequestMapping("/units")
public class UnitController {

    @Autowired
    private UnitRepo repo;

    @GetMapping
    public Page<UnitDTO> getUnits(Pageable pageable) {
        Page<UnitEntity> res = repo.findAll(pageable);
        return res.map(Mapper::toUnitDTO);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<Object> addUnit(@RequestBody UnitDTO dto) {
        try {
            UnitEntity entity = repo.save(Mapper.toUnitEntity(dto));
            return new ResponseEntity<>(Mapper.toUnitDTO(entity), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('superuser')")
    public ResponseEntity<Object> deleteUnit(@PathVariable String id) {
        try {
            UnitEntity entity = repo.findById(UUID.fromString(id)).orElse(null);
            if(entity == null) return ResponseEntity.badRequest().body("unit not found");
            repo.delete(entity);
            return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
