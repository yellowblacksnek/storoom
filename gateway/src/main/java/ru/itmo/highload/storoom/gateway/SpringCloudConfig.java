package ru.itmo.highload.storoom.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/token/**")
                        .uri("lb://TOKEN-SERVICE"))
                .route(r -> r.path("/users/**")
                        .uri("lb://USERS-SERVICE"))
                .build();
    }

}
