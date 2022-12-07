package ru.itmo.highload.storroom.files.clients;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.itmo.highload.storroom.files.dtos.NotificationMessage;

//@FeignClient(name = "NOTIFICATIONS")
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationClient {
    final WebClient.Builder webClientBuilder;
    public Mono<Void> sendNotification(@RequestBody NotificationMessage message) {
        log.info("sending notification");
        return webClientBuilder.build().post()
                .uri("http://NOTIFICATIONS/send")
                .bodyValue(message)
                .exchangeToMono(resp -> {
                    log.info("resp: {}",resp);
                    return Mono.empty();
                });
//        return Mono.empty();
    }
}
