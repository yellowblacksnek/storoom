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
import ru.itmo.highload.storroom.orders.dtos.external.locks.LockDTO;
import ru.itmo.highload.storroom.orders.dtos.external.locks.ManufacturerDTO;
import ru.itmo.highload.storroom.orders.dtos.orders.OrderDTO;
import ru.itmo.highload.storroom.orders.dtos.orders.OrderFullDTO;
import ru.itmo.highload.storroom.orders.dtos.external.users.UserDTO;
import ru.itmo.highload.storroom.orders.models.OrderEntity;
import ru.itmo.highload.storroom.orders.models.OrderStatus;
import ru.itmo.highload.storroom.orders.models.UnitEntity;
import ru.itmo.highload.storroom.orders.models.UnitStatus;
import ru.itmo.highload.storroom.orders.repositories.OrderRepository;
import ru.itmo.highload.storroom.orders.repositories.UnitRepo;
import ru.itmo.highload.storroom.orders.services.OrderService;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTests extends BaseTests{

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public ServiceInstanceListSupplier serviceInstanceListSupplier() {
            return new TestServiceInstanceListSupplier("random_text_why_not", 7567);
        }
    }

    @RegisterExtension
    static WireMockExtension USERS_SERVICE = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().port(7567))
            .build();

    private final static UUID UUID_ID = UUID.fromString("527af30f-a4cc-43b5-a2fc-745722aee05c");

    @BeforeEach
    public void setup() throws JsonProcessingException {
        UserDTO user = new UserDTO();
        LockDTO lock = new LockDTO();
        lock.setManufacturer(new ManufacturerDTO());
        USERS_SERVICE.stubFor(WireMock.get("/users/" + UUID_ID)
                .willReturn(WireMock.okJson(toJson(user))));
        USERS_SERVICE.stubFor(WireMock.get("/locks/" + UUID_ID)
                .willReturn(WireMock.okJson(toJson(lock))));
        USERS_SERVICE.stubFor(WireMock.get("/locations/" + UUID_ID)
                .willReturn(WireMock.okJson(toJson(lock))));
    }



    @Autowired private UnitRepo unitRepo;

    @Autowired private OrderRepository orderRepo;
    @Autowired private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAll() throws Exception {
        UnitEntity unit = unitRepo.findAll().iterator().next();

        OrderDTO orderDTO = new OrderDTO(null, LocalDateTime.now(), LocalDateTime.now().plusDays(20L), null, OrderStatus.active, unit.getId(), UUID_ID);
        orderService.create(orderDTO);

        ResultActions response = mockMvc.perform(get("/orders")
                .contentType(APPLICATION_JSON));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.totalElements").value(1));
        response.andExpect(jsonPath("$.content.size()").value(1));
    }

    @Test
    public void testGetByUserId() throws Exception {
        UserDTO user = new UserDTO();
        user.setUsername("testUser");
        USERS_SERVICE.stubFor(WireMock.get("/users/" + UUID_ID)
                .willReturn(WireMock.okJson(toJson(user))));
        UnitEntity unit = unitRepo.findAll().iterator().next();

        OrderDTO orderDTO = new OrderDTO(null, LocalDateTime.now(), LocalDateTime.now().plusDays(20L), null, OrderStatus.active, unit.getId(), UUID_ID);
        orderService.create(orderDTO);

        ResultActions response = mockMvc.perform(get("/orders")
                        .param("userId", String.valueOf(UUID_ID))
                        .param("authUsername", "testUser")
                        .param("isSuperuser", "false")
                .contentType(APPLICATION_JSON));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.totalElements").value(1));
        response.andExpect(jsonPath("$.content.size()").value(1));
    }

    @Test
    public void testGetByUserIdForbidden() throws Exception {
        UserDTO user = new UserDTO();
        user.setUsername("testUser");
        USERS_SERVICE.stubFor(WireMock.get("/users/" + UUID_ID)
                .willReturn(WireMock.okJson(toJson(user))));
        UnitEntity unit = unitRepo.findAll().iterator().next();

        OrderDTO orderDTO = new OrderDTO(null, LocalDateTime.now(), LocalDateTime.now().plusDays(20L), null, OrderStatus.active, unit.getId(), UUID_ID);
        orderService.create(orderDTO);

        ResultActions response = mockMvc.perform(get("/orders")
                .param("userId", String.valueOf(UUID_ID))
                .param("authUsername", "differentUsername")
                .param("isSuperuser", "false")
                .contentType(APPLICATION_JSON));

        response.andExpect(status().isForbidden());
    }

    @Test
    public void testCreateOrder() throws Exception {
        UnitEntity unit = unitRepo.findAll().iterator().next();

        OrderDTO orderDTO = new OrderDTO(null, LocalDateTime.now(), LocalDateTime.now().plusDays(20L), null, OrderStatus.active, unit.getId(), UUID_ID);

        ResultActions response = mockMvc.perform(post("/orders")
                .contentType(APPLICATION_JSON)
                .content(toJson(orderDTO)));

        response.andExpect(status().isCreated());

        assertEquals(1, orderRepo.count());
    }

    @Test
    public void testUpdateOrder() throws Exception {
        UnitEntity unit = unitRepo.findAll().iterator().next();

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusDays(1L);
        LocalDateTime endTimePlusDay = endTime.plusDays(1L);

        OrderDTO orderDTO = new OrderDTO(null, startTime, endTime, null, OrderStatus.active, unit.getId(), UUID_ID);
        OrderFullDTO orderFullDTO = orderService.create(orderDTO);

        orderDTO.setId(orderFullDTO.getId());
        orderDTO.setEndTime(endTimePlusDay);

        ResultActions response = mockMvc.perform(put("/orders/" + orderDTO.getId())
                .contentType(APPLICATION_JSON)
                .content(toJson(orderDTO)));

        response.andExpect(status().isOk());

        OrderEntity orderEntity = orderRepo.findAll().iterator().next();
        assertEquals(orderEntity.getEndTime().withNano(0), endTimePlusDay.withNano(0));
    }

    @Test
    public void testFinishOrder() throws Exception {
        UnitEntity unit = unitRepo.findAll().iterator().next();

        OrderDTO orderDTO = new OrderDTO(null, LocalDateTime.now(), LocalDateTime.now().plusDays(20L), null, OrderStatus.active, unit.getId(), UUID_ID);
        OrderFullDTO orderFullDTO = orderService.create(orderDTO);

        orderDTO.setId(orderFullDTO.getId());

        ResultActions response = mockMvc.perform(post("/orders/" + orderDTO.getId() + "/finish")
                .contentType(APPLICATION_JSON));

        response.andExpect(status().isOk());

        OrderEntity orderEntity = orderRepo.findAll().iterator().next();
        unit = unitRepo.findAll().iterator().next();
        assertEquals(OrderStatus.finished, orderEntity.getStatus());
        assertEquals(UnitStatus.pending, unit.getStatus());
    }

}
