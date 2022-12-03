package ru.itmo.highload.storromm.aggregator.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.highload.storromm.aggregator.dtos.orders.OrderDTO;
import ru.itmo.highload.storromm.aggregator.dtos.orders.OrderFullDTO;
import ru.itmo.highload.storromm.aggregator.dtos.orders.OrderInfoDTO;
import ru.itmo.highload.storromm.aggregator.dtos.units.UnitDTO;
import ru.itmo.highload.storromm.aggregator.dtos.units.UnitFullDTO;
import ru.itmo.highload.storromm.aggregator.dtos.units.UnitInfoDTO;
import ru.itmo.highload.storromm.aggregator.dtos.units.UnitStatusDTO;

import java.util.UUID;

@FeignClient("ORDERS-SERVICE")
public interface OrderClient {

        @GetMapping("/orders")
        ResponseEntity<Page<OrderDTO>> getAllOrders(Pageable pageable);

        @GetMapping("/orders")
        ResponseEntity<Page<OrderDTO>> getAllOrdersByUserId(@RequestParam UUID userId,
                                                     @RequestParam String authUsername,
                                                     @RequestParam Boolean isSuperuser,
                                                     Pageable pageable);

        @PostMapping("/orders")
        ResponseEntity<OrderFullDTO> createOrder(@RequestBody OrderDTO dto);

        @PutMapping("/orders/{id}")
        ResponseEntity<OrderFullDTO> updateOrder(@PathVariable UUID id, @RequestBody OrderInfoDTO dto);

        @PostMapping("/orders/{id}/finish")
        ResponseEntity<OrderFullDTO> finishOrder(@PathVariable UUID id);

        @GetMapping("/units")
        ResponseEntity<Page<UnitDTO>> getAllUnits(Pageable pageable);

        @PostMapping("/units")
        ResponseEntity<UnitFullDTO> createUnit(@RequestBody UnitDTO dto);

        @PutMapping("/units/{id}")
        ResponseEntity<UnitFullDTO> updateUnitInfo(@PathVariable UUID id, @RequestBody UnitInfoDTO dto);

        @PutMapping("/units/{id}/status")
        ResponseEntity<UnitFullDTO> updateUnitStatus(@PathVariable UUID id, @RequestBody UnitStatusDTO dto);

        @DeleteMapping("/units/{id}")
        ResponseEntity<UnitFullDTO> deleteUnit(@PathVariable UUID id);
}