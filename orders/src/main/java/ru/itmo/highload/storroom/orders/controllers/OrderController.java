package ru.itmo.highload.storroom.orders.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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


    @PreAuthorize("hasAuthority('superuser')")
    @GetMapping
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        return service.getAll(pageable);
    }

    @PreAuthorize("hasAuthority('client')")
    @GetMapping(params = "userId")
    public Page<OrderDTO> getAllByUserId(@RequestParam UUID userId, Authentication auth, Pageable pageable) {
        boolean isSuperuser = auth.getAuthorities().stream()
                .map(i -> i.toString())
                .anyMatch(i -> i.equals("superuser"));
        if(!isSuperuser && !userService.canViewOrders(userId, auth.getName())) {
            throw new ForbiddenException();
        }
        return service.getAllByUserId(userId, pageable);
    }


    @PreAuthorize("hasAuthority('client')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderFullDTO addOrder(@RequestBody OrderDTO dto) {
        return service.create(dto);
    }

    @PreAuthorize("hasAuthority('superuser')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderFullDTO updateOrder(@PathVariable UUID id, @RequestBody OrderDTO dto) {
        return service.updateOrderInfo(id, dto);
    }

    @PreAuthorize("hasAuthority('client')")
    @PostMapping("/{id}/finish")
    @ResponseStatus(HttpStatus.OK)
    public OrderFullDTO finishOrder(@PathVariable UUID id) {
        return service.finishOrder(id);
    }
}
