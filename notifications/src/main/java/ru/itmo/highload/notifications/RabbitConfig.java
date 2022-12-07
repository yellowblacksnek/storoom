package ru.itmo.highload.notifications;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.rabbitmq.*;

@Configuration
public class RabbitConfig {
    @Bean
    Sender rabbitSender() {
        return RabbitFlux.createSender();
    }
}