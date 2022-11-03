package ru.itmo.highload.storoom.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.itmo.highload.storoom.exceptions.ResourceAlreadyExistsException;
import ru.itmo.highload.storoom.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storoom.models.CompanyEntity;
import ru.itmo.highload.storoom.models.DTOs;
import ru.itmo.highload.storoom.models.DTOs.CompanyDTO;
import ru.itmo.highload.storoom.models.DTOs.CompanyReadDTO;
import ru.itmo.highload.storoom.models.OwnerEntity;
import ru.itmo.highload.storoom.repositories.CompanyRepository;
import ru.itmo.highload.storoom.repositories.OwnerRepo;
import ru.itmo.highload.storoom.utils.Mapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepo ownerRepo;
    private final CompanyRepository companyRepo;

    public Page<DTOs.OwnerReadDTO> getAll(Pageable pageable) {
        Page<OwnerEntity> res = ownerRepo.findAll(pageable);
        return res.map(Mapper::toOwnerDTO);
    }

    public OwnerEntity create(DTOs.OwnerDTO dto) {
        if (ownerRepo.existsByName(dto.getName())) {
            throw new ResourceAlreadyExistsException();
        }
        CompanyEntity company = companyRepo.findById(dto.getCompanyId()).orElseThrow(() -> new ResourceNotFoundException("company " + dto.getCompanyId() + " not found"));
        return ownerRepo.save(Mapper.toOwnerEntity(dto, company));
    }

    public OwnerEntity update(UUID id, DTOs.OwnerDTO dto) {
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new IllegalStateException("no name provided");
        }
        OwnerEntity ownerEntity = ownerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("company " + id + " not found"));
        ownerEntity.setName(dto.getName());
        return ownerRepo.save(ownerEntity);
    }

    public void delete(UUID id) {
        CompanyEntity companyEntity = ownerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("company " + id + " not found"));
        ownerRepo.delete(companyEntity);
    }
}
