package ru.itmo.highload.storoom.config;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResourceSizeAdvice implements ResponseBodyAdvice<Page<?>> {

    @Override
    public boolean supports(MethodParameter returnType, @Nullable Class<? extends HttpMessageConverter<?>> converterType) {
        return Page.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Page<?> beforeBodyWrite(Page<?> page,
                                   @Nullable MethodParameter methodParameter,
                                   @Nullable MediaType mediaType,
                                   @Nullable Class<? extends HttpMessageConverter<?>> aClass,
                                   @Nullable ServerHttpRequest serverHttpRequest,
                                   @Nullable ServerHttpResponse serverHttpResponse) {
        if (page != null && serverHttpResponse != null) {
            serverHttpResponse.getHeaders().add("X-Total-Count", String.valueOf(page.getTotalElements()));
        }
        return page;
    }

}