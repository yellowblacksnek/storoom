package ru.itmo.highload.storroom.locks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.itmo.highload.storroom.locks.dtos.LockDTO;
import ru.itmo.highload.storroom.locks.models.LockEntity;
import ru.itmo.highload.storroom.locks.models.ManufacturerEntity;
import ru.itmo.highload.storroom.locks.repositories.LockRepo;
import ru.itmo.highload.storroom.locks.repositories.ManufacturerRepo;
import ru.itmo.highload.storroom.locks.utils.Mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LockTests extends BaseTests{
    @Autowired private LockRepo repo;
    @Autowired private ManufacturerRepo manuRepo;
    @Autowired private MockMvc mockMvc;

    @Test
    public void testGetAll() throws Exception{
        String token = clientToken();
        ResultActions response = mockMvc.perform(get("/locks").header("Authorization", token));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.totalElements").value(1));
        response.andExpect(jsonPath("$.content.size()").value(1));
        response.andExpect(jsonPath("$.content[0].name").value("lock"));
    }

    @Test
    public void testAdd() throws Exception{
        ManufacturerEntity entity = manuRepo.findAll().iterator().next();
        LockDTO dto = new LockDTO();
        dto.setName("lock1");
        dto.setManufacturer(entity.getId());

        String token = superuserToken();
        ResultActions response = mockMvc.perform(post("/locks")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(dto)));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.name").value("lock1"));
        response.andExpect(jsonPath("$.manufacturer.name").value("manu"));

        assertEquals(2, repo.count());
    }

    @Test
    public void testUpdate() throws Exception{
        LockEntity entity = repo.findAll().iterator().next();
        entity.setName("new name");
        LockDTO dto = Mapper.toLockDTO(entity);

        String token = superuserToken();
        ResultActions response = mockMvc.perform(put("/locks/"+dto.getId())
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(dto)));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.name").value("new name"));

        entity = repo.findAll().iterator().next();
        assertEquals("new name", entity.getName());
    }

    @Test
    public void testDelete() throws Exception{
        ManufacturerEntity manufacturer = manuRepo.findAll().iterator().next();
        LockEntity lock = new LockEntity();
        lock.setName("lock1");
        lock.setManufacturer(manufacturer);
        lock = repo.save(lock);

        String token = superuserToken();
        ResultActions response = mockMvc.perform(delete("/locks/"+lock.getId())
                .header("Authorization", token));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.name").value("lock1"));
        response.andExpect(jsonPath("$.manufacturer.name").value("manu"));


        assertEquals(1, repo.count());
    }
}
