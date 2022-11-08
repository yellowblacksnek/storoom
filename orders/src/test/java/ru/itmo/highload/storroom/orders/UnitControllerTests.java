package ru.itmo.highload.storroom.orders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.itmo.highload.storroom.orders.dtos.UnitDTO;
import ru.itmo.highload.storroom.orders.models.UnitEntity;
import ru.itmo.highload.storroom.orders.models.UnitStatus;
import ru.itmo.highload.storroom.orders.repositories.UnitRepo;
import ru.itmo.highload.storroom.orders.utils.Mapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UnitControllerTests extends BaseTests{
    @Autowired private UnitRepo unitRepo;

    @Autowired
    private MockMvc mockMvc;

    private UnitEntity createUnit() {
        return new UnitEntity(
                10,10,10,
                UUID.randomUUID(),
                UnitStatus.available,
                UUID.randomUUID()
        );
    }

    @BeforeEach
    public void cleanUp() {
//        unitRepo.deleteAll();
//        locationRepo.deleteAll();
//        lockRepo.deleteAll();
//        manufacturerRepo.deleteAll();
    }

    @Test
    public void testGetUnits() throws Exception {
        String token = superuserToken();
        ResultActions response = mockMvc.perform(get("/units").header("Authorization", token));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    public void testAddUnit() throws Exception {
        UnitEntity unitEntity = createUnit();
        unitEntity.setId(UUID.randomUUID());
        UnitDTO dto = Mapper.toUnitDTO(unitEntity);

        String token = superuserToken();
        ResultActions response = mockMvc.perform(post("/units")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(dto)));

        response.andExpect(status().isCreated());

        assertEquals(2, unitRepo.count());
    }

    @Test
    public void testUpdateUnitInfo() throws Exception {
        UnitEntity unitEntity = createUnit();
        unitEntity = unitRepo.save(unitEntity);
        UnitDTO dto = Mapper.toUnitDTO(unitEntity);
        dto.setSizeX(20);

        String token = superuserToken();
        ResultActions response = mockMvc.perform(put("/units/" + unitEntity.getId().toString())
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(dto)));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.sizeX").value(20));

        assertEquals(2, unitRepo.count());
    }

    @Test
    public void testDeleteUnit() throws Exception {
        UnitEntity unitEntity = createUnit();
        unitEntity = unitRepo.save(unitEntity);
        String token = superuserToken();
        ResultActions response = mockMvc.perform(delete("/units/" + unitEntity.getId().toString())
                .header("Authorization", token));

        response.andExpect(status().isNoContent());
        assertEquals(1, unitRepo.count());
    }

}
