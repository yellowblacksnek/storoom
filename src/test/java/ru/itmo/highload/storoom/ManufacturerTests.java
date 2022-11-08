package ru.itmo.highload.storoom;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.itmo.highload.storoom.consts.UserType;
import ru.itmo.highload.storoom.models.DTOs;
import ru.itmo.highload.storoom.models.ManufacturerEntity;
import ru.itmo.highload.storoom.repositories.ManufacturerRepo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.itmo.highload.storoom.services.UserDetailsServiceImpl.getAuthorities;

public class ManufacturerTests extends BaseTests{
    @Autowired private ManufacturerRepo repo;
    @Autowired private MockMvc mockMvc;

    @Test
    public void testGetAll() throws Exception{
        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(get("/manufacturers").header("Authorization", token));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.totalElements").value(1));
        response.andExpect(jsonPath("$.content.size()").value(1));
        response.andExpect(jsonPath("$.content[0].name").value("manu"));
    }

    @Test
    public void testAdd() throws Exception{
        DTOs.ManufacturerDTO dto = new DTOs.ManufacturerDTO();
        dto.setName("manufacturer");

        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(post("/manufacturers")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(dto)));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.name").value("manufacturer"));

        assertEquals(2, repo.count());
    }

    @Test
    public void testUpdate() throws Exception{
        ManufacturerEntity entity = repo.findAll().iterator().next();
        entity.setName("new name");

        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(put("/manufacturers/"+entity.getId()+"/name")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(entity)));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.name").value("new name"));

        entity = repo.findAll().iterator().next();
        assertEquals("new name", entity.getName());
    }

    @Test
    public void testDelete() throws Exception{
        ManufacturerEntity entity = new ManufacturerEntity();
        entity.setName("manufacturer");
        entity = repo.save(entity);

        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(delete("/manufacturers/"+entity.getId())
                .header("Authorization", token));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.name").value("manufacturer"));

        assertEquals(1, repo.count());
    }
}
