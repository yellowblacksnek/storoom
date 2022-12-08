package ru.itmo.highload.storromm.aggregator.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storromm.aggregator.annotations.CustomizedOperation;
import ru.itmo.highload.storromm.aggregator.clients.OrderClient;
import ru.itmo.highload.storromm.aggregator.dtos.orders.OrderDTO;
import ru.itmo.highload.storromm.aggregator.dtos.orders.OrderFullDTO;
import ru.itmo.highload.storromm.aggregator.dtos.orders.OrderInfoDTO;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Orders")
public class OrderController {
    private final OrderClient orderClient;

//    @PreAuthorize("hasAuthority('superuser')")
//    @GetMapping
//    public ResponseEntity<Page<OrderDTO>> getAllOrders(Pageable pageable) {
//        return orderClient.getAllOrders(pageable);
//    }

    @PreAuthorize("hasAuthority('client')")
    @GetMapping(params = "userId")
    @CustomizedOperation(description = "Get orders", pageable = true, responseCodes = {400, 401, 403, 404})
    @Parameter(name = "userId", description = "(optional) userId to search for")
    public ResponseEntity<Page<OrderDTO>> getOrders(@RequestParam(required = false) UUID userId, Authentication auth, Pageable pageable) {
        if(userId == null) return orderClient.getAllOrders(pageable);
        boolean isSuperuser = auth.getAuthorities().stream()
                .map(Object::toString)
                .anyMatch(i -> i.equals("superuser"));
        return orderClient.getAllOrdersByUserId(userId, auth.getName(), isSuperuser, pageable);
    }


    @PreAuthorize("hasAuthority('client')")
    @PostMapping
    @CustomizedOperation(description = "Create order", responseCodes = {400, 401, 403, 404, 409})
    public ResponseEntity<OrderFullDTO> createOrder(@RequestBody OrderDTO dto) {
        return orderClient.createOrder(dto);
    }

    @PreAuthorize("hasAuthority('superuser')")
    @PutMapping("/{id}")
    @CustomizedOperation(description = "Update order", responseCodes = {400, 401, 403, 404, 409})
    public ResponseEntity<OrderFullDTO> updateOrder(@PathVariable UUID id, @RequestBody OrderInfoDTO dto) {
        return orderClient.updateOrder(id, dto);
    }

    @PreAuthorize("hasAuthority('client')")
    @PostMapping("/{id}/finish")
    @CustomizedOperation(description = "Finish order", responseCodes = {400, 401, 403, 404, 409})
    public ResponseEntity<OrderFullDTO> finishOrder(@PathVariable UUID id) {
        return orderClient.finishOrder(id);
    }
}
