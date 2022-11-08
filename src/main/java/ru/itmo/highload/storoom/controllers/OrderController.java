package ru.itmo.highload.storoom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storoom.models.DTOs.OrderDTO;
import ru.itmo.highload.storoom.services.OrderService;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService service;


    @PreAuthorize("hasAuthority('superuser')")
    @GetMapping
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        return service.getAll(pageable);
    }

    @PreAuthorize("(hasAuthority('client') && #username == auth.name) || hasAuthority('superuser')")
    @GetMapping("?user={username}")
    public Page<OrderDTO> getAllByUsername(@PathVariable String username, Authentication auth, Pageable pageable) {
        return service.getAllByUsername(username, pageable);
    }


    @PreAuthorize("hasAuthority('client')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO addOrder(@RequestBody OrderDTO dto) {
        return service.create(dto);
    }

    @PreAuthorize("hasAuthority('superuser')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO updateOrder(@PathVariable UUID id, @RequestBody OrderDTO dto) {
        return service.updateOrderInfo(id, dto);
    }

    @PreAuthorize("hasAuthority('client')")
    @PostMapping("/{id}/finish")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO finishOrder(@PathVariable UUID id) {
        return service.finishOrder(id);
    }
}
