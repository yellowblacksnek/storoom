package ru.itmo.highload.storroom.orders.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.itmo.highload.storroom.orders.dtos.LocationDTO;

import java.util.UUID;

@FeignClient(value = "LOCATIONS-SERVICE", fallback = LocationClientFallback.class)
@Primary
public interface LocationClient {
    @GetMapping(value = "/locations/{id}")
    LocationDTO getLocation(@RequestHeader(value = "Authorization") String authorizationHeader,
                            @PathVariable(value="id") UUID id);
}
