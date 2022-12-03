package ru.itmo.highload.storroom.orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.itmo.highload.storroom.orders.dtos.external.locations.LocationDTO;
import ru.itmo.highload.storroom.orders.dtos.external.locks.LockDTO;
import ru.itmo.highload.storroom.orders.dtos.external.locks.ManufacturerDTO;
import ru.itmo.highload.storroom.orders.dtos.units.UnitDTO;
import ru.itmo.highload.storroom.orders.models.UnitEntity;
import ru.itmo.highload.storroom.orders.models.UnitStatus;
import ru.itmo.highload.storroom.orders.repositories.UnitRepo;
import ru.itmo.highload.storroom.orders.utils.Mapper;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UnitControllerTests extends BaseTests{

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public ServiceInstanceListSupplier serviceInstanceListSupplier() {
            return new TestServiceInstanceListSupplier("random_text_why_not", 7568);
        }
    }

    @RegisterExtension
    static WireMockExtension LOCKS_SERVICE = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().port(7568))
            .build();

    private final static String UUID_ID = "527af30f-a4cc-43b5-a2fc-745722aee05c";

    @Autowired private UnitRepo unitRepo;

    @Autowired
    private MockMvc mockMvc;

    private UnitEntity createUnit() {
        return new UnitEntity(
                10,10,10,
                UUID.fromString(UUID_ID),
                UnitStatus.available,
                UUID.fromString(UUID_ID)
        );
    }

    @BeforeEach
    public void setup() throws JsonProcessingException {
        LockDTO lock = new LockDTO();
        lock.setManufacturer(new ManufacturerDTO());
        LocationDTO location = new LocationDTO();
        location.owners = new ArrayList<>();
        LOCKS_SERVICE.stubFor(WireMock.get("/locks/" + UUID_ID)
                .willReturn(WireMock.okJson(toJson(lock))));
        LOCKS_SERVICE.stubFor(WireMock.get("/locations/" + UUID_ID)
                .willReturn(WireMock.okJson(toJson(lock))));
    }

    @Test
    public void testGetUnits() throws Exception {
        ResultActions response = mockMvc.perform(get("/units"));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    public void testAddUnit() throws Exception {
        UnitEntity unitEntity = createUnit();
        unitEntity.setId(UUID.randomUUID());
        UnitDTO dto = Mapper.toUnitDTO(unitEntity);

        ResultActions response = mockMvc.perform(post("/units")
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

        ResultActions response = mockMvc.perform(put("/units/" + unitEntity.getId().toString())
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
        ResultActions response = mockMvc.perform(delete("/units/" + unitEntity.getId().toString()));

        response.andExpect(status().isNoContent());
        assertEquals(1, unitRepo.count());
    }

}
