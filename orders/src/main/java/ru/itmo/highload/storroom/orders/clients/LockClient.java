package ru.itmo.highload.storroom.orders.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.itmo.highload.storroom.orders.dtos.LockDTO;

import java.util.UUID;

@FeignClient(name = "LOCKS-SERVICE")
@Primary
public interface LockClient {
    @GetMapping(value = "/locks/{id}")
    LockDTO getLock(@RequestHeader(value = "Authorization") String authorizationHeader,
                    @PathVariable(value="id") UUID id);
}
