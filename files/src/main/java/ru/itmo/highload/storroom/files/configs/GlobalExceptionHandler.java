package ru.itmo.highload.storroom.files.configs;

import org.springframework.core.io.buffer.DataBufferLimitException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({DataBufferLimitException.class})
    public final Mono<ResponseEntity<Object>> handleFileTooLarge(Exception ex, ServerWebExchange exchange) {
        return handleExceptionInternal(ex, "File is too large.", new HttpHeaders(), HttpStatus.BAD_REQUEST, exchange);
    }
}
