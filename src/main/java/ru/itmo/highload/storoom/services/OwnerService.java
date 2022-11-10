package ru.itmo.highload.storoom.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.itmo.highload.storoom.exceptions.ResourceAlreadyExistsException;
import ru.itmo.highload.storoom.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storoom.models.CompanyEntity;
import ru.itmo.highload.storoom.models.DTOs;
import ru.itmo.highload.storoom.models.LocationEntity;
import ru.itmo.highload.storoom.models.OwnerEntity;
import ru.itmo.highload.storoom.repositories.CompanyRepository;
import ru.itmo.highload.storoom.repositories.LocationRepo;
import ru.itmo.highload.storoom.repositories.OwnerRepo;
import ru.itmo.highload.storoom.utils.Mapper;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepo ownerRepo;
    private final CompanyRepository companyRepo;
    private final LocationRepo locationRepo;

    public Page<DTOs.OwnerReadDTO> getAll(Pageable pageable) {
        Page<OwnerEntity> res = ownerRepo.findAll(pageable);
        return res.map(Mapper::toOwnerDTO);
    }

    public DTOs.OwnerReadDTO create(DTOs.OwnerDTO dto) {
        if (ownerRepo.existsByName(dto.getName())) {
            throw new ResourceAlreadyExistsException();
        }
        CompanyEntity company = companyRepo.findById(dto.getCompanyId()).orElseThrow(() -> new ResourceNotFoundException("Company", dto.getCompanyId()));
        List<LocationEntity> locations = locationRepo.findByIdIn(dto.getLocationIds());
        return Mapper.toOwnerDTO(ownerRepo.save(Mapper.toOwnerEntity(dto, company, locations)));
    }

    public DTOs.OwnerReadDTO update(UUID id, DTOs.OwnerDTO dto) {
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new IllegalStateException("no name provided");
        }
        OwnerEntity ownerEntity = ownerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Owner", id));
        ownerEntity.setName(dto.getName());
        CompanyEntity company = companyRepo.findById(dto.getCompanyId()).orElseThrow(() -> new ResourceNotFoundException("Company", dto.getCompanyId()));
        ownerEntity.setCompany(company);
        List<LocationEntity> locations = locationRepo.findByIdIn(dto.getLocationIds());
        ownerEntity.setLocations(locations);
        return Mapper.toOwnerDTO(ownerRepo.save(ownerEntity));
    }

    public void delete(UUID id) {
        OwnerEntity ownerEntity = ownerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Owner", id));
        ownerRepo.delete(ownerEntity);
    }
}
