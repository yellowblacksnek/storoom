package ru.itmo.highload.storroom.locations.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.locations.dtos.companies.CompanyDTO;
import ru.itmo.highload.storroom.locations.dtos.companies.CompanyReadDTO;
import ru.itmo.highload.storroom.locations.services.CompanyService;

import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    public Flux<CompanyReadDTO> getAllCompanies(Pageable pageable) {
        return companyService.getAll(pageable);
    }

    @PostMapping
    public Mono<CompanyReadDTO> addCompany(@RequestBody CompanyDTO req) {
        return companyService.create(req);
    }

    @PutMapping("{id}")
    public Mono<CompanyReadDTO> updateCompany(@PathVariable String id, @RequestBody CompanyDTO dto) {
        return companyService.update(UUID.fromString(id), dto);
    }

    @DeleteMapping("{id}")
    public Mono<CompanyReadDTO> deleteCompany(@PathVariable("id") String id) {
        return companyService.delete(UUID.fromString(id));
    }
}
