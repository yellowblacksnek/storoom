package ru.itmo.highload.storroom.locations;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.itmo.highload.storroom.locations.dtos.*;
import ru.itmo.highload.storroom.locations.models.LocationEntity;
import ru.itmo.highload.storroom.locations.models.OwnerEntity;
import ru.itmo.highload.storroom.locations.repositories.LocationRepository;
import ru.itmo.highload.storroom.locations.repositories.OwnerRepository;
import ru.itmo.highload.storroom.locations.utils.Mapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class LocationTests extends BaseTests{
    @Autowired
    private LocationRepository locationRepository;
    @Autowired private OwnerRepository ownerRepository;
    @Autowired private WebTestClient webTestClient;

    @Test
    public void testGetAll(){
        LocationCompactDTO location = webTestClient.get()
                .uri("/locations")
                .exchange()
                .expectStatus().isOk()
                .returnResult(LocationCompactDTO.class)
                .getResponseBody().blockFirst();

        assertEquals("address", location.getAddress());

    }

    @Test
    public void testAdd() throws Exception{
        LocationDTO dto = new LocationDTO();
        dto.setAddress("address1");

        LocationReadDTO res = webTestClient.post()
                .uri("/locations")
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(toJson(dto)))
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(LocationReadDTO.class)
                .getResponseBody()
                .blockFirst();

        assertEquals("address1", res.getAddress());
        assertEquals(2, locationRepository.count().block());
    }

    @Test
    public void testAddOwner() throws Exception{
        OwnerEntity ownerEntity = new OwnerEntity();
        ownerEntity.setName("owwner");
        ownerEntity.setCompanyId(UUID.fromString("527af30f-a4cc-43b5-a2fc-745722aee05c"));
        ownerEntity = ownerRepository.save(ownerEntity).block();

        LocationEntity entity = locationRepository.findAll().blockFirst();

        LocationReadDTO res = webTestClient.put()
                .uri("/locations/"+entity.getId()+"/owners/"+ownerEntity.getId())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(LocationReadDTO.class)
                .getResponseBody()
                .blockFirst();

        assertEquals(2, res.getOwners().size());
    }

    @Test
    public void testDeleteOwner() throws Exception{
        LocationEntity location = locationRepository.findAll().blockFirst();
        OwnerEntity owner = ownerRepository.findAll().blockFirst();

        LocationReadDTO res = webTestClient.delete()
                .uri("/locations/"+location.getId()+"/owners/"+owner.getId())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(LocationReadDTO.class)
                .getResponseBody()
                .blockFirst();

        assertEquals(0, res.getOwners().size());
    }

    @Test
    public void testUpdate() throws Exception{
        LocationEntity entity = locationRepository.findAll().blockFirst();
        LocationDTO dto = Mapper.toLocationDTO(entity);
        dto.setAddress("new address");

        LocationReadDTO res = webTestClient.put()
                .uri("/locations/"+entity.getId())
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(toJson(dto)))
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(LocationReadDTO.class)
                .getResponseBody()
                .blockFirst();

        assertEquals("new address", res.getAddress());
        assertEquals(1, res.getOwners().size());
        assertEquals("owner", res.getOwners().get(0).getName());

        entity = locationRepository.findAll().blockFirst();
        assertEquals("new address", entity.getAddress());

    }

    @Test
    public void testDelete(){
        LocationEntity entity = locationRepository.findAll().blockFirst();

        LocationReadDTO res = webTestClient.delete()
                .uri("/locations/"+entity.getId())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(LocationReadDTO.class)
                .getResponseBody()
                .blockFirst();

        assertEquals("address", res.getAddress());
        assertEquals(0, locationRepository.count().block());
    }
}