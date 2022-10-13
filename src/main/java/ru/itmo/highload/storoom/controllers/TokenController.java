package ru.itmo.highload.storoom.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.highload.storoom.utils.TokenUtils;

@Slf4j
@RestController
public class TokenController {

    @Autowired
    private TokenUtils utils;

    @PostMapping("/token")
    public String token(Authentication authentication) {

        long expireHours = 10;
        long expireSeconds = expireHours * 60 * 60;
        return utils.generateToken(authentication.getName(), authentication.getAuthorities(), expireSeconds);
    }



}