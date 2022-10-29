package ru.itmo.highload.storoom.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.itmo.highload.storoom.exceptions.BadRequestException;
import ru.itmo.highload.storoom.exceptions.ResourceAlreadyExistsException;
import ru.itmo.highload.storoom.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storoom.models.CompanyEntity;
import ru.itmo.highload.storoom.models.DTOs.CompanyDTO;
import ru.itmo.highload.storoom.models.DTOs.CompanyReadDTO;
import ru.itmo.highload.storoom.repositories.CompanyRepository;
import ru.itmo.highload.storoom.utils.Mapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepo;

    public Page<CompanyReadDTO> getAll(Pageable pageable) {
        Page<CompanyEntity> res = companyRepo.findAll(pageable);
        return res.map(Mapper::toCompanyDTO);
    }

    public CompanyEntity create(CompanyDTO dto) {
        if (companyRepo.existsByName(dto.getName())) {
            throw new ResourceAlreadyExistsException();
        }
        return companyRepo.save(Mapper.toCompanyEntity(dto));
    }

    public CompanyEntity update(UUID id, CompanyDTO dto) {
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new BadRequestException("no name provided");
        }
        CompanyEntity companyEntity = companyRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("company " + id + " not found"));
        companyEntity.setName(dto.getName());
        return companyRepo.save(companyEntity);
    }

    public void delete(UUID id) {
        CompanyEntity companyEntity = companyRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("company " + id + " not found"));
        companyRepo.delete(companyEntity);
    }
}
