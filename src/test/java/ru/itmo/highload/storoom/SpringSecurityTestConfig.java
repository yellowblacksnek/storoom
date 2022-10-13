package ru.itmo.highload.storoom;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

@TestConfiguration
public class SpringSecurityTestConfig {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {

        UserDetails user1 = User.builder()
                .username("client")
                .password(encoder.encode("test"))
                .authorities("client")
                .build();

        UserDetails user2 = User.builder()
                .username("superuser")
                .password(encoder.encode("test"))
                .authorities("superuser")
                .build();


        return new InMemoryUserDetailsManager(Arrays.asList(
                user1, user2
        ));
    }
}
