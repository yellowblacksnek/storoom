package ru.itmo.highload.storroom.token.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.highload.storroom.token.utils.JwtHelper;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TokenController {

    private final JwtHelper jwtHelper;

    @PostMapping("/token")
    public String token(Authentication authentication) {

        long expireHours = 10;
        long expireSeconds = expireHours * 60 * 60;
        return jwtHelper.generateToken(authentication.getName(), authentication.getAuthorities(), expireSeconds);
    }



}
