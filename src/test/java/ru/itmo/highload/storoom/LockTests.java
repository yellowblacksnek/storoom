package ru.itmo.highload.storoom;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.itmo.highload.storoom.consts.UserType;
import ru.itmo.highload.storoom.models.DTOs;
import ru.itmo.highload.storoom.models.LockEntity;
import ru.itmo.highload.storoom.models.ManufacturerEntity;
import ru.itmo.highload.storoom.repositories.LockRepo;
import ru.itmo.highload.storoom.repositories.ManufacturerRepo;
import ru.itmo.highload.storoom.utils.Mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.itmo.highload.storoom.services.UserDetailsServiceImpl.getAuthorities;

public class LockTests extends BaseTests{
    @Autowired private LockRepo repo;
    @Autowired private ManufacturerRepo manuRepo;
    @Autowired private MockMvc mockMvc;

    @Test
    public void testGetAll() throws Exception{
        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(get("/locks").header("Authorization", token));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.totalElements").value(1));
        response.andExpect(jsonPath("$.content.size()").value(1));
        response.andExpect(jsonPath("$.content[0].name").value("lock"));
    }

    @Test
    public void testAdd() throws Exception{
        ManufacturerEntity entity = manuRepo.findAll().iterator().next();
        DTOs.LockDTO dto = new DTOs.LockDTO();
        dto.setName("lock1");
        dto.setManufacturer(entity.getId());

        String token = getToken("user", getAuthorities(UserType.superuser));
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
        DTOs.LockDTO dto = Mapper.toLockDTO(entity);

        String token = getToken("user", getAuthorities(UserType.superuser));
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

        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(delete("/locks/"+lock.getId())
                .header("Authorization", token));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.name").value("lock1"));
        response.andExpect(jsonPath("$.manufacturer.name").value("manu"));


        assertEquals(1, repo.count());
    }
}
