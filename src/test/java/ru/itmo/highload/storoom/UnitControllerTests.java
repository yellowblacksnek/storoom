package ru.itmo.highload.storoom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.itmo.highload.storoom.consts.UnitStatus;
import ru.itmo.highload.storoom.consts.UserType;
import ru.itmo.highload.storoom.models.LocationEntity;
import ru.itmo.highload.storoom.models.LockEntity;
import ru.itmo.highload.storoom.models.UnitEntity;
import ru.itmo.highload.storoom.repositories.LocationRepo;
import ru.itmo.highload.storoom.repositories.LockRepo;
import ru.itmo.highload.storoom.repositories.UnitRepo;
import ru.itmo.highload.storoom.utils.Mapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.itmo.highload.storoom.models.DTOs.UnitDTO;
import static ru.itmo.highload.storoom.services.UserDetailsServiceImpl.getAuthorities;

public class UnitControllerTests extends BaseTests{

    @Autowired private LocationRepo locationRepo;
    @Autowired private LockRepo lockRepo;

    @Autowired private UnitRepo unitRepo;

    @Autowired
    private MockMvc mockMvc;

    private UnitEntity createUnit() {

        LockEntity lockEntity = lockRepo.findAll().iterator().next();
        LocationEntity locationEntity = locationRepo.findAll().iterator().next();

        return new UnitEntity(
                10,10,10,
                locationEntity,
                UnitStatus.available,
                lockEntity
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
        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(get("/units").header("Authorization", token));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    public void testAddUnit() throws Exception {
        UnitEntity unitEntity = createUnit();
        unitEntity.setId(UUID.randomUUID());
        UnitDTO dto = Mapper.toUnitDTO(unitEntity);

        String token = getToken("user", getAuthorities(UserType.superuser));
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

        String token = getToken("user", getAuthorities(UserType.superuser));
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
        String token = getToken("user", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(delete("/units/" + unitEntity.getId().toString())
                .header("Authorization", token));

        response.andExpect(status().isNoContent());
        assertEquals(1, unitRepo.count());
    }

}
