package ru.itmo.highload.storoom;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.itmo.highload.storoom.consts.OrderStatus;
import ru.itmo.highload.storoom.consts.UnitStatus;
import ru.itmo.highload.storoom.consts.UserType;
import ru.itmo.highload.storoom.models.*;
import ru.itmo.highload.storoom.repositories.*;
import ru.itmo.highload.storoom.services.OrderService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.itmo.highload.storoom.services.UserDetailsServiceImpl.getAuthorities;

public class OrderControllerTests extends BaseTests{

    @Autowired private UnitRepo unitRepo;

    @Autowired private UserRepository userRepo;

    @Autowired private OrderRepository orderRepo;
    @Autowired private OrderService orderService;


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAll() throws Exception {
        UserEntity user = userRepo.findAll().iterator().next();
        UnitEntity unit = unitRepo.findAll().iterator().next();

        DTOs.OrderDTO orderDTO = new DTOs.OrderDTO(null, LocalDateTime.now(), LocalDateTime.now().plusDays(20L), null, OrderStatus.active, unit.getId(), user.getId());
        orderService.create(orderDTO);

        String token = getToken("name", getAuthorities(UserType.superuser));
        ResultActions response = mockMvc.perform(get("/orders")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON));

        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.totalElements").value(1));
        response.andExpect(jsonPath("$.content.size()").value(1));
    }

    @Test
    public void testCreateOrder() throws Exception {
        UserEntity user = userRepo.findAll().iterator().next();
        UnitEntity unit = unitRepo.findAll().iterator().next();

        DTOs.OrderDTO orderDTO = new DTOs.OrderDTO(null, LocalDateTime.now(), LocalDateTime.now().plusDays(20L), null, OrderStatus.active, unit.getId(), user.getId());

        String token = getToken("name", getAuthorities(UserType.client));
        ResultActions response = mockMvc.perform(post("/orders")
                .header("Authorization", token)
                .contentType(APPLICATION_JSON)
                .content(toJson(orderDTO)));

        response.andExpect(status().isCreated());

        assertEquals(1, orderRepo.count());
    }

    @Test
    public void testUpdateOrder() throws Exception {
        UserEntity user = userRepo.findAll().iterator().next();
        UnitEntity unit = unitRepo.findAll().iterator().next();

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusDays(1L);
        LocalDateTime endTimePlusDay = endTime.plusDays(1L);

        DTOs.OrderDTO orderDTO = new DTOs.OrderDTO(null, startTime, endTime, null, OrderStatus.active, unit.getId(), user.getId());
        orderDTO = orderService.create(orderDTO);

        orderDTO.setEndTime(endTimePlusDay);

        String token = getToken("name", getAuthorities(UserType.superuser));
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
        UserEntity user = userRepo.findAll().iterator().next();
        UnitEntity unit = unitRepo.findAll().iterator().next();

        DTOs.OrderDTO orderDTO = new DTOs.OrderDTO(null, LocalDateTime.now(), LocalDateTime.now().plusDays(20L), null, OrderStatus.active, unit.getId(), user.getId());
        orderDTO = orderService.create(orderDTO);

        String token = getToken("name", getAuthorities(UserType.superuser));
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
