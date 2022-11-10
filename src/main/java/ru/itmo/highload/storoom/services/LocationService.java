package ru.itmo.highload.storoom.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.itmo.highload.storoom.consts.UnitStatus;
import ru.itmo.highload.storoom.exceptions.ResourceAlreadyExistsException;
import ru.itmo.highload.storoom.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storoom.models.DTOs;
import ru.itmo.highload.storoom.models.LocationEntity;
import ru.itmo.highload.storoom.models.OwnerEntity;
import ru.itmo.highload.storoom.models.UnitEntity;
import ru.itmo.highload.storoom.repositories.LocationRepo;
import ru.itmo.highload.storoom.repositories.OwnerRepo;
import ru.itmo.highload.storoom.repositories.UnitRepo;
import ru.itmo.highload.storoom.utils.Mapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepo locationRepo;
    private final UnitRepo unitRepo;
    private final OwnerRepo ownerRepo;

    public LocationEntity getEntityById(UUID id) {
        return locationRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Location", id));
    }

    public Page<DTOs.LocationReadDTO> getAll(Pageable pageable) {
        Page<LocationEntity> res = locationRepo.findAll(pageable);
        return res.map(Mapper::toLocationDTO);
    }

    public DTOs.LocationReadDTO create(DTOs.LocationDTO dto) {
        if (locationRepo.existsByAddress(dto.getAddress())) {
            throw new ResourceAlreadyExistsException();
        }
        List<OwnerEntity> owners = getOwnersEntities(dto.getOwnerIds());
        return Mapper.toLocationDTO(locationRepo.save(Mapper.toLocationEntity(dto, owners)));
    }

    public DTOs.LocationReadDTO update(UUID id, DTOs.LocationDTO dto) {
        if (dto.getAddress() == null || dto.getAddress().isEmpty()) {
            throw new IllegalStateException("no name provided");
        }
        LocationEntity locationEntity = getEntityById(id);
        locationEntity.setAddress(dto.getAddress());
        locationEntity.setLocationType(dto.getLocationType());
        List<OwnerEntity> owners = getOwnersEntities(dto.getOwnerIds());
        locationEntity.setOwners(owners);
        return Mapper.toLocationDTO(locationRepo.save(locationEntity));
    }

    public void delete(UUID id) {
        LocationEntity locationEntity = getEntityById(id);
        List<UnitEntity> units = unitRepo.getAllByLocation(locationEntity);
        if (units.stream().anyMatch(u -> u.getStatus().equals(UnitStatus.occupied))) {
            throw new IllegalArgumentException("Close all units orders before deleting the location");
        }
        locationRepo.delete(locationEntity);
    }

    private List<OwnerEntity> getOwnersEntities(List<UUID> ids) {
        return ownerRepo.findByIdIn(ids)
                .stream()
                .map(Optional::of)
                .map(ownerEntity -> ownerEntity.orElseThrow(() -> new ResourceNotFoundException("owner " + ownerEntity.get().getId() + " not found")))
                .collect(Collectors.toList());
    }
}
