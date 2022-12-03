package ru.itmo.highload.storromm.aggregator.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storromm.aggregator.clients.OrderClient;
import ru.itmo.highload.storromm.aggregator.dtos.orders.OrderDTO;
import ru.itmo.highload.storromm.aggregator.dtos.orders.OrderFullDTO;
import ru.itmo.highload.storromm.aggregator.dtos.orders.OrderInfoDTO;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderClient orderClient;

    @PreAuthorize("hasAuthority('superuser')")
    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getAllOrders(Pageable pageable) {
        return orderClient.getAllOrders(pageable);
    }

    @PreAuthorize("hasAuthority('client')")
    @GetMapping(params = "userId")
    public ResponseEntity<Page<OrderDTO>> getAllByUserId(@RequestParam UUID userId, Authentication auth, Pageable pageable) {
        boolean isSuperuser = auth.getAuthorities().stream()
                .map(Object::toString)
                .anyMatch(i -> i.equals("superuser"));
        return orderClient.getAllOrdersByUserId(userId, auth.getName(), isSuperuser, pageable);
    }


    @PreAuthorize("hasAuthority('client')")
    @PostMapping
    public ResponseEntity<OrderFullDTO> add(@RequestBody OrderDTO dto) {
        return orderClient.createOrder(dto);
    }

    @PreAuthorize("hasAuthority('superuser')")
    @PutMapping("/{id}")
    public ResponseEntity<OrderFullDTO> update(@PathVariable UUID id, @RequestBody OrderInfoDTO dto) {
        return orderClient.updateOrder(id, dto);
    }

    @PreAuthorize("hasAuthority('client')")
    @PostMapping("/{id}/finish")
    public ResponseEntity<OrderFullDTO> finish(@PathVariable UUID id) {
        return orderClient.finishOrder(id);
    }
}
