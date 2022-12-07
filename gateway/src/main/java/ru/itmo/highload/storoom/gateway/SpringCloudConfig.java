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
                .route(r -> r.path("/ws")
                        .uri("lb://NOTIFICATIONS-WS"))
                .route(r -> r.path("/files/**")
                        .uri("lb://AGGREGATOR-REACTIVE"))
                .route(r -> r.path("/**")
                        .uri("lb://AGGREGATOR"))
                .build();
    }

}
