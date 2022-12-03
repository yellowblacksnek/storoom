package ru.itmo.highload.storroom.locations;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.itmo.highload.storroom.locations.dtos.owners.OwnerCompactDTO;
import ru.itmo.highload.storroom.locations.dtos.owners.OwnerDTO;
import ru.itmo.highload.storroom.locations.dtos.owners.OwnerReadDTO;
import ru.itmo.highload.storroom.locations.models.OwnerEntity;
import ru.itmo.highload.storroom.locations.repositories.OwnerRepository;
import ru.itmo.highload.storroom.locations.utils.Mapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class OwnerTests extends BaseTests{
    @Autowired
    private WebTestClient webTestClient;
    @Autowired private OwnerRepository ownerRepository;

    @Test
    public void testGetAll(){
        OwnerCompactDTO owner = webTestClient.get()
                .uri("/owners")
                .exchange()
                .expectStatus().isOk()
                .returnResult(OwnerCompactDTO.class)
                .getResponseBody().blockFirst();

        assertEquals("owner", owner.getName());
    }

    @Test
    public void testAdd() throws Exception{
        OwnerDTO dto = new OwnerDTO();
        dto.setName("owner1");
        dto.setCompanyId(UUID.fromString("527af30f-a4cc-43b5-a2fc-745722aee05c"));

        OwnerReadDTO res = webTestClient.post()
                .uri("/owners")
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(toJson(dto)))
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(OwnerReadDTO.class)
                .getResponseBody()
                .blockFirst();

        assertEquals("owner1", res.getName());
        assertEquals(2, ownerRepository.count().block());
    }

    @Test
    public void testUpdate() throws Exception{
        OwnerEntity entity = ownerRepository.findAll().blockFirst();
        OwnerDTO dto = Mapper.toOwnerDTO(entity);
        dto.setName("new name");

        OwnerReadDTO res = webTestClient.put()
                .uri("/owners/"+entity.getId())
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(toJson(dto)))
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(OwnerReadDTO.class)
                .getResponseBody()
                .blockFirst();

        assertEquals("new name", res.getName());
        assertEquals(1, res.getLocations().size());
        assertEquals("address", res.getLocations().get(0).getAddress());

        entity = ownerRepository.findAll().blockFirst();
        assertEquals("new name", entity.getName());

    }

    @Test
    public void testDelete(){
        OwnerEntity entity = ownerRepository.findAll().blockFirst();

        OwnerReadDTO res = webTestClient.delete()
                .uri("/owners/"+entity.getId())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(OwnerReadDTO.class)
                .getResponseBody()
                .blockFirst();

        assertEquals("owner", res.getName());
        assertEquals(0, ownerRepository.count().block());
    }
}
