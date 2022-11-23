package ru.itmo.highload.storromm.aggregator.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@FeignClient("ORDERS-SERVICE")
public interface OrderClient {

        @GetMapping("/orders")
        ResponseEntity<Object> getAllOrders(Pageable pageable);

        @GetMapping("/orders")
        ResponseEntity<Object> getAllOrdersByUserId(@RequestParam UUID userId,
                                                     @RequestParam String authUsername,
                                                     @RequestParam Boolean isSuperuser,
                                                     Pageable pageable);

        @PostMapping("/orders")
        ResponseEntity<Object> createOrder(@RequestBody Map<String, String> dto);

        @PutMapping("/orders/{id}")
        ResponseEntity<Object> updateOrder(@PathVariable UUID id, @RequestBody Map<String, String> dto);

        @PostMapping("/orders/{id}/finish")
        ResponseEntity<Object> finishOrder(@PathVariable UUID id);

        @GetMapping("/units")
        ResponseEntity<Object> getAllUnits(Pageable pageable);

        @PostMapping("/units")
        ResponseEntity<Object> createUnit(@RequestBody Map<String, String> dto);

        @PutMapping("/units/{id}")
        ResponseEntity<Object> updateUnitInfo(@PathVariable UUID id, @RequestBody Map<String, String> dto);

        @PutMapping("/units/{id}/status")
        ResponseEntity<Object> updateUnitStatus(@PathVariable UUID id, @RequestBody Map<String, String> dto);

        @DeleteMapping("/units/{id}")
        ResponseEntity<Object> deleteUnit(@PathVariable UUID id);
}