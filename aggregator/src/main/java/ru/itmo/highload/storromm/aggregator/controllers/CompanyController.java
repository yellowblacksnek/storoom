package ru.itmo.highload.storromm.aggregator.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storromm.aggregator.clients.LocationClient;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/companies")
@PreAuthorize("hasAuthority('superuser')")
@RequiredArgsConstructor
public class CompanyController {
    private final LocationClient companyClient;

    @GetMapping
    public Flux<Object> getAllCompanies(Pageable pageable) {
        return companyClient.getAllCompanies(pageable);
    }

    @PostMapping
    public Mono<Object> addCompany(@RequestBody Map<String,String> req) {
        return companyClient.addCompany(req);
    }

    @PutMapping("{id}")
    public Mono<Object> updateCompany(@PathVariable String id, @RequestBody Map<String,String> dto) {
        return companyClient.updateCompany(id, dto);
    }

    @DeleteMapping("{id}")
    public Mono<Object> deleteCompany(@PathVariable("id") String id) {
        return companyClient.deleteCompany(id);
    }
}
