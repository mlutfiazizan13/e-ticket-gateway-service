package com.mla.eticket.gatewayservice.filter;


import com.mla.eticket.gatewayservice.utils.JwtUtil;
import com.mla.eticket.gatewayservice.validator.RouteValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<Object> {

    Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    public final JwtUtil jwtUtil;
    public final RouteValidator routeValidator;

    @Value("${authentication.enabled}")
    private boolean authEnabled;


    @Autowired
    public AuthenticationFilter(JwtUtil jwtUtil, RouteValidator routeValidator) {
        this.jwtUtil = jwtUtil;
        this.routeValidator = routeValidator;
    }


    @Override
    public GatewayFilter apply(Object config) {
        log.info("Check Hit");
        return ((exchange, chain) -> {
            log.info(String.valueOf(exchange.getRequest().getURI()));
            log.info(String.valueOf(routeValidator.isSecured.test(exchange.getRequest())));
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("missing authorization header");
                }
                String authHeader = exchange.getRequest().getHeaders().get(org.apache.http.HttpHeaders.AUTHORIZATION).get(0);
                log.info(authHeader);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                try {
                    log.info("Check Token");
                    jwtUtil.validateToken(authHeader);
                    log.info("Check Token Finish");
                } catch (Exception e) {
                    System.out.println("invalid access...!");
                    log.info("Invalid Access....!");
                    throw new RuntimeException("un authorized access to application");
                }

            }
            log.info("Check Hit Finish");

            return chain.filter(exchange);
        });
    }


}
