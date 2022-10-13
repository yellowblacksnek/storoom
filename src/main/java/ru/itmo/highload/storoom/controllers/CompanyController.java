package ru.itmo.highload.storoom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storoom.models.CompanyEntity;
import ru.itmo.highload.storoom.repositories.CompanyRepository;
import ru.itmo.highload.storoom.utils.Mapper;

import java.util.Optional;
import java.util.UUID;

import static ru.itmo.highload.storoom.models.DTOs.CompanyDTO;
import static ru.itmo.highload.storoom.models.DTOs.CompanyReadDTO;

@RestController
@RequestMapping("/companies")
@PreAuthorize("hasAuthority('superuser')")
public class CompanyController {
    @Autowired
    private CompanyRepository companyRepo;

    @GetMapping
    public Page<CompanyReadDTO> getAllCompanies(Pageable pageable) {
        Page<CompanyEntity> res = companyRepo.findAll(pageable);
        return res.map(Mapper::toCompanyDTO);
    }

    @PostMapping
    public ResponseEntity addCompany(@RequestBody CompanyDTO req) {
        if (companyRepo.existsByName(req.getName())) {
            return ResponseEntity.badRequest().body("this company already exists");
        }
        req.setName(req.getName());
        companyRepo.save(Mapper.toCompanyEntity(req));

        return new ResponseEntity("", HttpStatus.CREATED);
    }

    @PatchMapping("companies/{id}")
    public ResponseEntity updateCompany(@PathVariable UUID id, @RequestParam String name) {
        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().body("no name provided");
        }
        Optional<CompanyEntity> company = companyRepo.findById(id);
        if (company.isEmpty()) {
            return ResponseEntity.badRequest().body("username not found");
        }
        company.get().setName(name);
        companyRepo.save(company.get());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("companies/{id}")
    public ResponseEntity deleteCompany(@PathVariable UUID id) {
        Optional<CompanyEntity> company = companyRepo.findById(id);
        if (company.isEmpty()) {
            return ResponseEntity.badRequest().body("company not found");
        }
        companyRepo.delete(company.get());

        return ResponseEntity.ok().build();
    }
}
