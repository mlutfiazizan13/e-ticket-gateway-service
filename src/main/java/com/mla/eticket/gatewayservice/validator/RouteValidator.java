package com.mla.eticket.gatewayservice.validator;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> unprotectedURLs = List.of("/auth/login", "/auth/register", "/eureka");

    public Predicate<ServerHttpRequest> isSecured = serverHttpRequest -> unprotectedURLs.stream().noneMatch(uri -> serverHttpRequest.getURI().getPath().contains(uri));
}
