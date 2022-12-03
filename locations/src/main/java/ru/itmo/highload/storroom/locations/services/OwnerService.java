package ru.itmo.highload.storroom.locations.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.locations.dtos.owners.OwnerCompactDTO;
import ru.itmo.highload.storroom.locations.dtos.owners.OwnerDTO;
import ru.itmo.highload.storroom.locations.dtos.owners.OwnerInfoDTO;
import ru.itmo.highload.storroom.locations.dtos.owners.OwnerReadDTO;
import ru.itmo.highload.storroom.locations.exceptions.ResourceAlreadyExistsException;
import ru.itmo.highload.storroom.locations.exceptions.ResourceNotFoundException;
import ru.itmo.highload.storroom.locations.models.OwnerEntity;
import ru.itmo.highload.storroom.locations.repositories.OwnerRepository;
import ru.itmo.highload.storroom.locations.utils.Mapper;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepo;
    private final OwnerLocationService ownerLocationService;

    public Mono<OwnerReadDTO> getById(UUID id) {
        Mono<OwnerEntity> entity =  ownerRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("owner", String.valueOf(id))));
        return Mono.zip(entity, ownerLocationService.getLocationsCompact(id),
                Mapper::toOwnerReadDTO);
    }

    public Flux<OwnerCompactDTO> getAll(Pageable pageable) {
        return ownerRepo.findAll().map(Mapper::toOwnerCompactDTO);
    }

    public Mono<OwnerReadDTO> create(OwnerDTO dto) {
        return ownerRepo.existsByName(dto.getName())
                .map(exists -> {
                    if(exists) throw new ResourceAlreadyExistsException();
                    return false;
                })
                .then(ownerRepo.save(Mapper.toOwnerEntity(dto)))
                .map(i -> Mapper.toOwnerReadDTO(i, new ArrayList<>()));
    }

    public Mono<OwnerReadDTO> addLocation(UUID id, UUID locationId) {
        return ownerLocationService.add(id, locationId)
                .then(Mono.zip(ownerRepo.findById(id)
                                .switchIfEmpty(Mono.error(new ResourceNotFoundException("owner", String.valueOf(id)))),
                        ownerLocationService.getLocationsCompact(id),
                        Mapper::toOwnerReadDTO));
    }

    public Mono<OwnerReadDTO> deleteLocation(UUID id, UUID locationId) {
        return ownerLocationService.delete(id, locationId)
                .then(Mono.zip(ownerRepo.findById(id)
                                .switchIfEmpty(Mono.error(new ResourceNotFoundException("owner", String.valueOf(id)))),
                        ownerLocationService.getLocationsCompact(id),
                        Mapper::toOwnerReadDTO));
    }

    public Mono<OwnerReadDTO> update(UUID id, OwnerInfoDTO dto) {
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new IllegalStateException("no name provided");
        }
        Mono<OwnerEntity> entity =  ownerRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("owner", String.valueOf(id))))
                .map(i -> {
                    i.setName(dto.getName());
                    i.setCompanyId(dto.getCompanyId());
                    return i;
                })
                .flatMap(ownerRepo::save);

        return Mono.zip(entity, ownerLocationService.getLocationsCompact(id),
                Mapper::toOwnerReadDTO);
    }

    public Mono<OwnerReadDTO> delete(UUID id) {
        Mono<OwnerEntity> entity =  ownerRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("owner", String.valueOf(id))))
                .flatMap(i -> ownerRepo.delete(i).thenReturn(i));

        return Mono.zip(entity, ownerLocationService.getLocationsCompact(id),
                Mapper::toOwnerReadDTO);
    }


}
