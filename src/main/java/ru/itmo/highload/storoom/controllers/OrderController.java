package ru.itmo.highload.storoom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storoom.models.DTOs;
import ru.itmo.highload.storoom.models.OrderEntity;
import ru.itmo.highload.storoom.repositories.OrderRepository;
import ru.itmo.highload.storoom.utils.Mapper;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderRepository orderRepo;


    @PreAuthorize("hasAuthority('superuser' || 'admin')")
    @GetMapping
    public Page<DTOs.OrderReadDTO> getAllOrders(Pageable pageable) {
        Page<OrderEntity> res = orderRepo.findAll(pageable);
        return res.map(Mapper::toOrderDTO);
    }

    @PreAuthorize("hasAuthority('user')")
    @GetMapping
    public Page<DTOs.OrderReadDTO> getMyOrders(Authentication auth, Pageable pageable) {
        Page<OrderEntity> res = orderRepo.findAllByUser(pageable, auth.getName());
        return res.map(Mapper::toOrderDTO);
    }


    @PreAuthorize("hasAuthority('superuser' || 'admin')")
    @PostMapping
    public ResponseEntity addOrder(@RequestBody DTOs.OrderFullDTO req) {
        if (orderRepo.existsByNumber(req.getNumber())) {
            return ResponseEntity.badRequest().body("this order already exists");
        }
        req.setNumber(req.getNumber());
        orderRepo.save(Mapper.toOrderEntity(req));

        return new ResponseEntity("", HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('user')")
    @PostMapping
    public ResponseEntity addMyOrder(Authentication auth, @RequestBody DTOs.OrderFullDTO req) {
        if (orderRepo.existsByNumber(req.getNumber())) {
            return ResponseEntity.badRequest().body("this order already exists");
        }
        req.setNumber(req.getNumber());
        orderRepo.save(Mapper.toOrderEntity(req));

        return new ResponseEntity("", HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('superuser' || 'admin')")
    @PatchMapping("orders/{id}")
    public ResponseEntity updateOrder(@RequestParam DTOs.OrderFullDTO req) {
        if (req == null) {
            return ResponseEntity.badRequest().body("no order provided");
        }
        Optional<OrderEntity> order = orderRepo.findById(req.id);
        if (order.isEmpty()) {
            return ResponseEntity.badRequest().body("order not found");
        }
        order.get().setUser(req.user);
        order.get().setUnit(req.unit);
        order.get().setDays(req.days);
        order.get().setNumber(req.number);
        orderRepo.save(order.get());

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('user')")
    @PatchMapping("orders/{id}")
    public ResponseEntity updateMyOrder(@RequestParam DTOs.OrderFullDTO req) {
        if (req == null) {
            return ResponseEntity.badRequest().body("no order provided");
        }
        Optional<OrderEntity> order = orderRepo.findById(req.id);
        if (order.isEmpty()) {
            return ResponseEntity.badRequest().body("order not found");
        }
        order.get().setNumber(req.number);
        orderRepo.save(order.get());

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('superuser' || 'admin')")
    @DeleteMapping("orders/{id}")
    public ResponseEntity deleteOrder(@PathVariable UUID id) {
        Optional<OrderEntity> order = orderRepo.findById(id);
        if (order.isEmpty()) {
            return ResponseEntity.badRequest().body("order not found");
        }
        orderRepo.delete(order.get());

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('user')")
    @DeleteMapping("orders/{id}")
    public ResponseEntity deleteMyOrder(@PathVariable UUID id) {
        Optional<OrderEntity> order = orderRepo.findById(id);
        if (order.isEmpty()) {
            return ResponseEntity.badRequest().body("order not found");
        }
        orderRepo.delete(order.get());

        return ResponseEntity.ok().build();
    }
}
