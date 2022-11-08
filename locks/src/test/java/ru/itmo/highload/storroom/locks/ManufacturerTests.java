package ru.itmo.highload.storroom.locks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import ru.itmo.highload.storroom.locks.models.ManufacturerEntity;
import ru.itmo.highload.storroom.locks.dtos.ManufacturerDTO;
import ru.itmo.highload.storroom.locks.repositories.ManufacturerRepo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class ManufacturerTests extends BaseTests{
    @Autowired private ManufacturerRepo repo;
    @Autowired private WebTestClient webTestClient;
    @Autowired private MockMvc mockMvc;

    @Test
    public void testGetAll(){
        String token = clientToken();
        Flux<ManufacturerDTO> flux = webTestClient.get()
                .uri("/manufacturers")
                .header("Authorization", token)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ManufacturerDTO.class)
                .getResponseBody();
        List<ManufacturerDTO> res = flux.collectList().block();

        assertEquals(1, res.size());
        assertEquals("manu", res.get(0).getName());
    }

    @Test
    public void testAdd() throws Exception{
        ManufacturerDTO dto = new ManufacturerDTO();
        dto.setName("manufacturer");

        String token = superuserToken();
        ManufacturerDTO res = webTestClient.post()
                .uri("/manufacturers")
                .header("Authorization", token)
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

        String token = superuserToken();

        ManufacturerDTO res = webTestClient.put()
                .uri("/manufacturers/"+entity.getId()+"/name")
                .header("Authorization", token)
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
    public void testDelete() throws Exception{
        ManufacturerEntity entity = new ManufacturerEntity();
        entity.setName("manufacturer");
        entity = repo.save(entity);

        String token = superuserToken();
        ManufacturerDTO res = webTestClient.delete()
                .uri("/manufacturers/"+entity.getId())
                .header("Authorization", token)
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
