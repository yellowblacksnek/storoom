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
import ru.itmo.highload.storoom.services.LocationService;
import ru.itmo.highload.storoom.utils.ResponseHandler;

import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("/locations")
@PreAuthorize("hasAuthority('superuser')")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    public Page<DTOs.LocationReadDTO> getAllLocations(Pageable pageable) {
        return locationService.getAll(pageable);
    }

    @PostMapping
    public ResponseEntity<Object> addLocation(@RequestBody DTOs.LocationDTO req) {
        DTOs.LocationReadDTO locationEntity = locationService.create(req);
        return ResponseHandler.generateResponse("Successfully created location!", HttpStatus.OK, locationEntity);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateLocation(@PathVariable String id, @RequestBody DTOs.LocationDTO dto) {
        DTOs.LocationReadDTO locationEntity = locationService.update(UUID.fromString(id), dto);
        return ResponseHandler.generateResponse("Successfully updated location!", HttpStatus.OK, locationEntity);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteLocation(@PathVariable("id") String id) {
        locationService.delete(UUID.fromString(id));
        return ResponseHandler.generateResponse("Successfully deleted location! " + id, HttpStatus.OK, null);
    }
}