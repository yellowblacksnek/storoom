package ru.itmo.highload.storroom.locks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.itmo.highload.storroom.locks.dtos.LockDTO;
import ru.itmo.highload.storroom.locks.dtos.LockFullDTO;
import ru.itmo.highload.storroom.locks.models.LockEntity;
import ru.itmo.highload.storroom.locks.models.ManufacturerEntity;
import ru.itmo.highload.storroom.locks.repositories.LockRepo;
import ru.itmo.highload.storroom.locks.repositories.ManufacturerRepo;
import ru.itmo.highload.storroom.locks.utils.Mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class LockTests extends BaseTests{
    @Autowired private LockRepo repo;
    @Autowired private ManufacturerRepo manuRepo;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testGetAll(){
        webTestClient.get()
                .uri("/locks")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.numberOfElements").isEqualTo(1)
                .jsonPath("$.content[0].name").isEqualTo("lock");

    }

    @Test
    public void testAdd() throws Exception{
        ManufacturerEntity entity = manuRepo.findAll().iterator().next();
        LockDTO dto = new LockDTO();
        dto.setName("lock1");
        dto.setManufacturer(entity.getId());

        LockFullDTO res = webTestClient.post()
                .uri("/locks")
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(toJson(dto)))
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(LockFullDTO.class)
                .getResponseBody()
                .blockFirst();

        assertEquals("lock1", res.getName());
        assertEquals("manu", res.getManufacturer().getName());
        assertEquals(2, repo.count());
    }

    @Test
    public void testUpdate() throws Exception{
        LockEntity entity = repo.findAll().iterator().next();
        LockDTO dto = Mapper.toLockDTO(entity);
        dto.setName("new name");

        LockFullDTO res = webTestClient.put()
                .uri("/locks/"+dto.getId())
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(toJson(dto)))
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(LockFullDTO.class)
                .getResponseBody()
                .blockFirst();

        assertEquals("new name", res.getName());

        entity = repo.findAll().iterator().next();
        assertEquals("new name", entity.getName());
    }

    @Test
    public void testDelete(){
        ManufacturerEntity manufacturer = manuRepo.findAll().iterator().next();
        LockEntity lock = new LockEntity();
        lock.setName("lock1");
        lock.setManufacturer(manufacturer);
        lock = repo.save(lock);

        LockFullDTO res = webTestClient.delete()
                .uri("/locks/"+lock.getId())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(LockFullDTO.class)
                .getResponseBody()
                .blockFirst();

        assertEquals("lock1", res.getName());
        assertEquals("manu", res.getManufacturer().getName());
        assertEquals(1, repo.count());
    }
}
