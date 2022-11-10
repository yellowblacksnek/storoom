package ru.itmo.highload.storoom.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.itmo.highload.storoom.models.CompanyEntity;

import java.util.UUID;

public interface CompanyRepository extends PagingAndSortingRepository<CompanyEntity, UUID> {
    CompanyEntity findByName(String name);

    Boolean existsByName(String name);
}
