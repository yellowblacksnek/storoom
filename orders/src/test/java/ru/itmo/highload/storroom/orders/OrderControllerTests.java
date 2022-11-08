package ru.itmo.highload.storroom.orders;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.itmo.highload.storroom.orders.clients.UserClient;
import ru.itmo.highload.storroom.orders.dtos.OrderDTO;
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

//    @RegisterExtension
//    static WireMockExtension USERS_SERVICE = WireMockExtension.newInstance()
//            .options(WireMockConfiguration.wireMockConfig().port(9876))
//            .build();

    @Autowired private UnitRepo unitRepo;

    @Autowired private OrderRepository orderRepo;
    @Autowired private OrderService orderService;

    @Autowired private UserClient userClient;


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAll() throws Exception {
        UnitEntity unit = unitRepo.findAll().iterator().next();

        OrderDTO orderDTO = new OrderDTO(null, LocalDateTime.now(), LocalDateTime.now().plusDays(20L), null, OrderStatus.active, unit.getId(), UUID.randomUUID());
        orderService.create(orderDTO);

        String token = superuserToken();
        ResultActions response = mockMvc.perform(get("/orders")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.totalElements").value(1));
        response.andExpect(jsonPath("$.content.size()").value(1));
    }

    @Test
    public void testCreateOrder() throws Exception {
        UnitEntity unit = unitRepo.findAll().iterator().next();

        OrderDTO orderDTO = new OrderDTO(null, LocalDateTime.now(), LocalDateTime.now().plusDays(20L), null, OrderStatus.active, unit.getId(), UUID.randomUUID());

        String token = clientToken();
        ResultActions response = mockMvc.perform(post("/orders")
                .header("Authorization", token)
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

        OrderDTO orderDTO = new OrderDTO(null, startTime, endTime, null, OrderStatus.active, unit.getId(), UUID.randomUUID());
        orderDTO = orderService.create(orderDTO);

        orderDTO.setEndTime(endTimePlusDay);

        String token = superuserToken();
        ResultActions response = mockMvc.perform(put("/orders/" + orderDTO.getId())
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(orderDTO)));

        response.andExpect(status().isOk());

        OrderEntity orderEntity = orderRepo.findAll().iterator().next();
        assertEquals(orderEntity.getEndTime().withNano(0), endTimePlusDay.withNano(0));
    }

    @Test
    public void testFinishOrder() throws Exception {
        UnitEntity unit = unitRepo.findAll().iterator().next();

        OrderDTO orderDTO = new OrderDTO(null, LocalDateTime.now(), LocalDateTime.now().plusDays(20L), null, OrderStatus.active, unit.getId(), UUID.randomUUID());
        orderDTO = orderService.create(orderDTO);

        String token = superuserToken();
        ResultActions response = mockMvc.perform(post("/orders/" + orderDTO.getId() + "/finish")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON));

        response.andExpect(status().isOk());

        OrderEntity orderEntity = orderRepo.findAll().iterator().next();
        unit = unitRepo.findAll().iterator().next();
        assertEquals(OrderStatus.finished, orderEntity.getStatus());
        assertEquals(UnitStatus.pending, unit.getStatus());
    }

}
