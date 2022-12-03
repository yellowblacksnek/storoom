package ru.itmo.highload.storroom.locks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.itmo.highload.storroom.locks.models.ManufacturerEntity;
import ru.itmo.highload.storroom.locks.dtos.manufacturers.ManufacturerDTO;
import ru.itmo.highload.storroom.locks.repositories.ManufacturerRepo;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class ManufacturerTests extends BaseTests{
    @Autowired private ManufacturerRepo repo;
    @Autowired private WebTestClient webTestClient;

    @Test
    public void testGetAll(){
        webTestClient.get()
                .uri("/manufacturers")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.numberOfElements").isEqualTo(1)
                .jsonPath("$.content[0].name").isEqualTo("manu");
    }

    @Test
    public void testAdd() throws Exception{
        ManufacturerDTO dto = new ManufacturerDTO();
        dto.setName("manufacturer");

        ManufacturerDTO res = webTestClient.post()
                .uri("/manufacturers")
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(toJson(dto)))
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ManufacturerDTO.class)
                .getResponseBody()
                .blockFirst();

        assertEquals("manufacturer", res.getName());
        assertEquals(2, repo.count());
    }

    @Test
    public void testUpdate() throws Exception{
        ManufacturerEntity entity = repo.findAll().iterator().next();
        entity.setName("new name");

        ManufacturerDTO res = webTestClient.put()
                .uri("/manufacturers/"+entity.getId()+"/name")
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(toJson(entity)))
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ManufacturerDTO.class)
                .getResponseBody()
                .blockFirst();

        assertEquals("new name", res.getName());
        entity = repo.findAll().iterator().next();
        assertEquals("new name", entity.getName());
    }

    @Test
    public void testDelete() {
        ManufacturerEntity entity = new ManufacturerEntity();
        entity.setName("manufacturer");
        entity = repo.save(entity);

        ManufacturerDTO res = webTestClient.delete()
                .uri("/manufacturers/"+entity.getId())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ManufacturerDTO.class)
                .getResponseBody()
                .blockFirst();

        assertEquals("manufacturer", res.getName());
        assertEquals(1, repo.count());
    }
}
