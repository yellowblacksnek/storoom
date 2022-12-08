package ru.itmo.highload.storromm.aggregator.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.highload.storromm.aggregator.annotations.CustomizedOperation;
import ru.itmo.highload.storromm.aggregator.clients.TokenClient;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
@Tag(name = "token")
@SecurityRequirement(name = "Basic Authentication")
public class TokenController {
    private final TokenClient tokenClient;
    @PostMapping
    @CustomizedOperation(description = "Get JWT token by providing credentials via Basic auth.", responseCodes = {400, 401})
    public String token(HttpServletRequest req) {
        return tokenClient.token(req.getHeader("Authorization"));
    }
}
