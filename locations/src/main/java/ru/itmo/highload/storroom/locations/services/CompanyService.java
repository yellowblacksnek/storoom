package ru.itmo.highload.storroom.locations.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.locations.dtos.companies.CompanyDTO;
import ru.itmo.highload.storroom.locations.dtos.companies.CompanyReadDTO;
import ru.itmo.highload.storroom.locations.exceptions.ResourceAlreadyExistsException;
import ru.itmo.highload.storroom.locations.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storroom.locations.models.CompanyEntity;
import ru.itmo.highload.storroom.locations.repositories.CompanyRepository;
import ru.itmo.highload.storroom.locations.utils.Mapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepo;

    public Mono<CompanyReadDTO> getById(UUID id) {
        return companyRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("location", String.valueOf(id))))
                .map(Mapper::toCompanyDTO);
    }

    public Flux<CompanyReadDTO> getAll(Pageable pageable) {
        Flux<CompanyEntity> res = companyRepo.findAllBy(pageable);
        return res.map(Mapper::toCompanyDTO);
    }

    public Mono<CompanyReadDTO> create(CompanyDTO dto) {
        return companyRepo.existsByName(dto.getName())
                .map(exists -> {
                    if(exists) throw new ResourceAlreadyExistsException();
                    return false;
                })
                .then(companyRepo.save(Mapper.toCompanyEntity(dto)))
                .map(Mapper::toCompanyDTO);
    }

    public Mono<CompanyReadDTO> update(UUID id, CompanyDTO dto) {
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new IllegalArgumentException("no name provided");
        }

        return companyRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("company " + id + " not found")))
                .map(i -> {
                    i.setName(dto.getName());
                    return i;
                })
                .flatMap(companyRepo::save)
                .map(Mapper::toCompanyDTO);
    }

    public Mono<CompanyReadDTO> delete(UUID id) {
        return companyRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("company " + id + " not found")))
                .flatMap(i -> companyRepo.delete(i).thenReturn(Mapper.toCompanyDTO(i)));
    }
}
