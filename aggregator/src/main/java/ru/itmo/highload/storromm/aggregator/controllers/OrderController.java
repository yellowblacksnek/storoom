package ru.itmo.highload.storromm.aggregator.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storromm.aggregator.clients.OrderClient;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderClient orderClient;

    @PreAuthorize("hasAuthority('superuser')")
    @GetMapping
    public ResponseEntity<Object> getAllOrders( Pageable pageable) {
        return orderClient.getAllOrders(pageable);
    }

    @PreAuthorize("hasAuthority('client')")
    @GetMapping(params = "userId")
    public ResponseEntity<Object> getAllByUserId(@RequestParam UUID userId, Authentication auth, Pageable pageable) {
        boolean isSuperuser = auth.getAuthorities().stream()
                .map(Object::toString)
                .anyMatch(i -> i.equals("superuser"));
        return orderClient.getAllOrdersByUserId(userId, auth.getName(), isSuperuser, pageable);
    }


    @PreAuthorize("hasAuthority('client')")
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Map<String, String> dto) {
        return orderClient.createOrder(dto);
    }

    @PreAuthorize("hasAuthority('superuser')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody Map<String, String> dto) {
        return orderClient.updateOrder(id, dto);
    }

    @PreAuthorize("hasAuthority('client')")
    @PostMapping("/{id}/finish")
    public ResponseEntity<Object> finish(@PathVariable UUID id) {
        return orderClient.finishOrder(id);
    }
}
