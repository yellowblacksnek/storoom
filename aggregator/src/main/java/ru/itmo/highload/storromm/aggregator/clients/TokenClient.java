package ru.itmo.highload.storromm.aggregator.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "TOKEN-SERVICE")
public interface TokenClient {
    @PostMapping(value = "/token")
    String token(@RequestHeader(value = "Authorization") String authorizationHeader);
}
