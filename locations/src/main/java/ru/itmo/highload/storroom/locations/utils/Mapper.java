package ru.itmo.highload.storroom.locations.utils;

import ru.itmo.highload.storroom.locations.dtos.companies.CompanyDTO;
import ru.itmo.highload.storroom.locations.dtos.companies.CompanyReadDTO;
import ru.itmo.highload.storroom.locations.dtos.locations.LocationCompactDTO;
import ru.itmo.highload.storroom.locations.dtos.locations.LocationDTO;
import ru.itmo.highload.storroom.locations.dtos.locations.LocationReadDTO;
import ru.itmo.highload.storroom.locations.dtos.owners.OwnerCompactDTO;
import ru.itmo.highload.storroom.locations.dtos.owners.OwnerDTO;
import ru.itmo.highload.storroom.locations.dtos.owners.OwnerReadDTO;
import ru.itmo.highload.storroom.locations.models.CompanyEntity;
import ru.itmo.highload.storroom.locations.models.LocationEntity;
import ru.itmo.highload.storroom.locations.models.OwnerEntity;

import java.util.List;

public class Mapper {
    public static CompanyReadDTO toCompanyDTO(CompanyEntity entity) {
        CompanyReadDTO dto = new CompanyReadDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public static CompanyEntity toCompanyEntity(CompanyDTO dto) {
        CompanyEntity company = new CompanyEntity();
        company.setName(dto.getName());
        return company;
    }

    public static OwnerReadDTO toOwnerReadDTO(OwnerEntity entity, List<LocationCompactDTO> locations) {
        OwnerReadDTO dto = new OwnerReadDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCompanyId(entity.getCompanyId());
        dto.setLocations(locations);
        return dto;
    }

    public static OwnerCompactDTO toOwnerCompactDTO(OwnerEntity entity) {
        OwnerCompactDTO dto = new OwnerCompactDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCompanyId(entity.getCompanyId());
        return dto;
    }

    public static OwnerDTO toOwnerDTO(OwnerEntity entity) {
        OwnerDTO dto = new OwnerDTO();
        dto.setName(entity.getName());
        dto.setCompanyId(entity.getCompanyId());
        return dto;
    }

    public static OwnerEntity toOwnerEntity(OwnerDTO dto) {
        OwnerEntity owner = new OwnerEntity();
        owner.setName(dto.getName());
        owner.setCompanyId(dto.getCompanyId());
        return owner;
    }

    public static LocationDTO toLocationDTO(LocationEntity entity) {
        LocationDTO dto = new LocationDTO();
        dto.setAddress(entity.getAddress());
        dto.setLocationType(entity.getLocationType());
        return dto;
    }

    public static LocationReadDTO toLocationReadDTO(LocationEntity entity, List<OwnerCompactDTO> owners) {
        LocationReadDTO dto = new LocationReadDTO();
        dto.setId(entity.getId());
        dto.setAddress(entity.getAddress());
        dto.setLocationType(entity.getLocationType());
        dto.setOwners(owners);
        return dto;
    }

    public static LocationCompactDTO toLocationCompactDTO(LocationEntity entity) {
        LocationCompactDTO dto = new LocationCompactDTO();
        dto.setId(entity.getId());
        dto.setAddress(entity.getAddress());
        dto.setLocationType(entity.getLocationType());
        return dto;
    }

    public static LocationEntity toLocationEntity(LocationDTO dto) {
        LocationEntity location = new LocationEntity();
        location.setAddress(dto.getAddress());
        location.setLocationType(dto.locationType);
        return location;
    }
}
