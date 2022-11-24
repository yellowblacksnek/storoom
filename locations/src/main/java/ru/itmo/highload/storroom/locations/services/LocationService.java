package ru.itmo.highload.storroom.locations.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.locations.dtos.*;
import ru.itmo.highload.storroom.locations.exceptions.ResourceAlreadyExistsException;
import ru.itmo.highload.storroom.locations.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storroom.locations.models.LocationEntity;
import ru.itmo.highload.storroom.locations.repositories.LocationRepository;
import ru.itmo.highload.storroom.locations.utils.Mapper;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepo;
    private final OwnerLocationService ownerLocationService;

    public Mono<LocationReadDTO> getById(UUID id) {
        Mono<LocationEntity> entity =  locationRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("location", String.valueOf(id))));
        return Mono.zip(entity, ownerLocationService.getOwnersCompact(id),
                Mapper::toLocationReadDTO);
    }

    public Flux<LocationCompactDTO> getAll(Pageable pageable) {
        return locationRepo.findAll()
                .map(Mapper::toLocationCompactDTO);
    }

    public Mono<LocationReadDTO> create(LocationDTO dto) {
        return locationRepo.existsByAddress(dto.getAddress())
                .map(exists -> {
                    if(exists) throw new ResourceAlreadyExistsException();
                    return false;
                })
                .then(locationRepo.save(Mapper.toLocationEntity(dto)))
                .map(i -> Mapper.toLocationReadDTO(i, new ArrayList<>()));
    }

    public Mono<LocationReadDTO> addOwner(UUID id, UUID ownerId) {
        return ownerLocationService.add(ownerId, id)
                .then(Mono.zip(locationRepo.findById(id)
                                .switchIfEmpty(Mono.error(new ResourceNotFoundException("location", String.valueOf(id)))),
                        ownerLocationService.getOwnersCompact(id),
                        Mapper::toLocationReadDTO));
    }

    public Mono<LocationReadDTO> deleteOwner(UUID id, UUID ownerId) {
        return ownerLocationService.delete(ownerId, id)
                .then(Mono.zip(locationRepo.findById(id)
                                .switchIfEmpty(Mono.error(new ResourceNotFoundException("location", String.valueOf(id)))),
                        ownerLocationService.getOwnersCompact(id),
                        Mapper::toLocationReadDTO));
    }

    public Mono<LocationReadDTO> update(UUID id, LocationDTO dto) {
        if (dto.getAddress() == null || dto.getAddress().isEmpty()) {
            throw new IllegalStateException("no name provided");
        }
        Mono<LocationEntity> entity =  locationRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("location", String.valueOf(id))))
                .map(i -> {
                    i.setAddress(dto.getAddress());
                    i.setLocationType(dto.getLocationType());
                    return i;
                })
                .flatMap(locationRepo::save);

        return Mono.zip(entity, ownerLocationService.getOwnersCompact(id),
                Mapper::toLocationReadDTO);
    }

    public Mono<LocationReadDTO> delete(UUID id) {
        Mono<LocationEntity> entity =  locationRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("location ", String.valueOf(id))))
                .flatMap(i -> locationRepo.delete(i).thenReturn(i));

        return Mono.zip(entity, ownerLocationService.getOwnersCompact(id),
                Mapper::toLocationReadDTO);
    }
}
