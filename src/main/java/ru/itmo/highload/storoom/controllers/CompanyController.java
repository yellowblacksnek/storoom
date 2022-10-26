package ru.itmo.highload.storoom.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storoom.models.CompanyEntity;
import ru.itmo.highload.storoom.services.CompanyService;
import ru.itmo.highload.storoom.utils.ResponseHandler;

import java.util.UUID;

import static ru.itmo.highload.storoom.models.DTOs.CompanyDTO;
import static ru.itmo.highload.storoom.models.DTOs.CompanyReadDTO;

@Slf4j
@RestController
@RequestMapping("/companies")
@PreAuthorize("hasAuthority('superuser')")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    public Page<CompanyReadDTO> getAllCompanies(Pageable pageable) {
        return companyService.getAll(pageable);
    }

    @PostMapping
    public ResponseEntity<Object> addCompany(@RequestBody CompanyDTO req) {
        CompanyEntity companyEntity = companyService.create(req);
        return ResponseHandler.generateResponse("Successfully created company!", HttpStatus.OK, companyEntity);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateCompany(@PathVariable String id, @RequestBody CompanyDTO dto) {
        CompanyEntity companyEntity = companyService.update(UUID.fromString(id), dto);
        return ResponseHandler.generateResponse("Successfully updated company!", HttpStatus.OK, companyEntity);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteCompany(@PathVariable("id") String id) {
        companyService.delete(UUID.fromString(id));
        return ResponseHandler.generateResponse("Successfully deleted company!", HttpStatus.OK, null);
    }
}
