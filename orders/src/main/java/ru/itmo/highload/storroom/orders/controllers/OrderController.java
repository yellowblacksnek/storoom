package ru.itmo.highload.storroom.orders.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storroom.orders.dtos.OrderDTO;
import ru.itmo.highload.storroom.orders.dtos.OrderFullDTO;
import ru.itmo.highload.storroom.orders.exceptions.ForbiddenException;
import ru.itmo.highload.storroom.orders.services.OrderService;
import ru.itmo.highload.storroom.orders.services.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService service;
    @Autowired private UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<OrderDTO> getAllOrders(@ParameterObject Pageable pageable) {
        return service.getAll(pageable);
    }

    @GetMapping(params = "userId")
    @ResponseStatus(HttpStatus.OK)
    public Page<OrderDTO> getAllByUserId(@RequestParam UUID userId,
                                         @RequestParam String authUsername,
                                         @RequestParam Boolean isSuperuser,
                                         Pageable pageable) {
        if(!isSuperuser && !userService.canViewOrders(userId, authUsername)) {
            throw new ForbiddenException();
        }
        return service.getAllByUserId(userId, pageable);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderFullDTO addOrder(@RequestBody OrderDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderFullDTO updateOrder(@PathVariable UUID id, @RequestBody OrderDTO dto) {
        return service.updateOrderInfo(id, dto);
    }

    @PostMapping("/{id}/finish")
    @ResponseStatus(HttpStatus.OK)
    public OrderFullDTO finishOrder(@PathVariable UUID id) {
        return service.finishOrder(id);
    }
}
