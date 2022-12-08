package ru.itmo.highload.storromm.aggregator.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storromm.aggregator.annotations.CustomizedOperation;
import ru.itmo.highload.storromm.aggregator.clients.LocationClient;
import ru.itmo.highload.storromm.aggregator.dtos.companies.CompanyDTO;
import ru.itmo.highload.storromm.aggregator.dtos.companies.CompanyReadDTO;

@Slf4j
@RestController
@RequestMapping("/companies")
@PreAuthorize("hasAuthority('superuser')")
@RequiredArgsConstructor
@Tag(name = "Companies")
public class CompanyController {
    private final LocationClient companyClient;

    @GetMapping
    @CustomizedOperation(description = "Get all companies.", pageable = true, responseCodes = {401, 403})
    public Flux<CompanyReadDTO> getAllCompanies(Pageable pageable) {
        return companyClient.getAllCompanies(pageable);
    }

    @PostMapping
    @CustomizedOperation(description = "Add company.", responseCodes = {400, 401, 403})
    public Mono<CompanyReadDTO> addCompany(@RequestBody CompanyDTO req) {
        return companyClient.addCompany(req);
    }

    @PutMapping("{id}")
    @CustomizedOperation(description = "Update company's info.", responseCodes = {400, 401, 403, 404, 409})
    public Mono<CompanyReadDTO> updateCompany(@PathVariable String id, @RequestBody CompanyDTO dto) {
        return companyClient.updateCompany(id, dto);
    }

    @DeleteMapping("{id}")
    @CustomizedOperation(description = "Delete company.", responseCodes = {400, 401, 403, 404, 409})
    public Mono<CompanyReadDTO> deleteCompany(@PathVariable("id") String id) {
        return companyClient.deleteCompany(id);
    }
}
