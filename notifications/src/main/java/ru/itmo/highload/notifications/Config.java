package ru.itmo.highload.notifications;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public Queue myQueue() {
        return new Queue("myQueue", false);
    }
}
