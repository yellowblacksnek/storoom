package ru.itmo.highload.storroom.token.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtHelper {
    private final JwtEncoder encoder;

    public String generateToken(String username, Collection<? extends GrantedAuthority> authoritiesList, Long expireSeconds) {

        Instant now = Instant.now();
        // @formatter:off
        String authorities = authoritiesList.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expireSeconds))
                .subject(username)
                .claim("scope", authorities)
                .build();
        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();
        // @formatter:on
        return this.encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

}
