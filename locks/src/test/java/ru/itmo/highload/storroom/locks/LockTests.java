package ru.itmo.highload.storroom.locks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import ru.itmo.highload.storroom.locks.dtos.LockDTO;
import ru.itmo.highload.storroom.locks.dtos.LockFullDTO;
import ru.itmo.highload.storroom.locks.models.LockEntity;
import ru.itmo.highload.storroom.locks.models.ManufacturerEntity;
import ru.itmo.highload.storroom.locks.repositories.LockRepo;
import ru.itmo.highload.storroom.locks.repositories.ManufacturerRepo;
import ru.itmo.highload.storroom.locks.utils.Mapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class LockTests extends BaseTests{
    @Autowired private LockRepo repo;
    @Autowired private ManufacturerRepo manuRepo;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testGetAll(){
        String token = superuserToken();

        Flux<LockFullDTO> flux = webTestClient.get()
                .uri("/locks")
                .header("Authorization", token)
                .exchange()
                .expectStatus().isOk()
                .returnResult(LockFullDTO.class)
                .getResponseBody();

        List<LockFullDTO> res = flux.collectList().block();

        assertEquals(1, res.size());
        assertEquals("lock", res.get(0).getName());
    }

    @Test
    public void testAdd() throws Exception{
        ManufacturerEntity entity = manuRepo.findAll().iterator().next();
        LockDTO dto = new LockDTO();
        dto.setName("lock1");
        dto.setManufacturer(entity.getId());

        String token = superuserToken();
        LockFullDTO res = webTestClient.post()
                .uri("/locks")
                .header("Authorization", token)
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

        String token = superuserToken();

        LockFullDTO res = webTestClient.put()
                .uri("/locks/"+dto.getId())
                .header("Authorization", token)
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

        String token = superuserToken();
        LockFullDTO res = webTestClient.delete()
                .uri("/locks/"+lock.getId())
                .header("Authorization", token)
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
