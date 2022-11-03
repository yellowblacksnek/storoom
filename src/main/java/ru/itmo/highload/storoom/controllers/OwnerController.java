package ru.itmo.highload.storoom.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storoom.models.DTOs;
import ru.itmo.highload.storoom.models.OwnerEntity;
import ru.itmo.highload.storoom.services.OwnerService;
import ru.itmo.highload.storoom.utils.ResponseHandler;

import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("/companies")
@PreAuthorize("hasAuthority('superuser')")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;

    @GetMapping
    public Page<DTOs.OwnerReadDTO> getAllOwners(Pageable pageable) {
        return ownerService.getAll(pageable);
    }

    @PostMapping
    public ResponseEntity<Object> addOwner(@RequestBody DTOs.OwnerDTO req) {
        OwnerEntity ownerEntity = ownerService.create(req);
        return ResponseHandler.generateResponse("Successfully created company!", HttpStatus.OK, ownerEntity);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateOwner(@PathVariable String id, @RequestBody DTOs.OwnerDTO dto) {
        OwnerEntity ownerEntity = ownerService.update(UUID.fromString(id), dto);
        return ResponseHandler.generateResponse("Successfully updated company!", HttpStatus.OK, ownerEntity);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteOwner(@PathVariable("id") String id) {
        ownerService.delete(UUID.fromString(id));
        return ResponseHandler.generateResponse("Successfully deleted company!", HttpStatus.OK, null);
    }
}