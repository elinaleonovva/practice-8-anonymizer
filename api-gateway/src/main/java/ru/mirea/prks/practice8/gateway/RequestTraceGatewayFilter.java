package ru.mirea.prks.practice8.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/** Creates an X-Request-ID at the system boundary and forwards it to every service. */
@Component
public class RequestTraceGatewayFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(RequestTraceGatewayFilter.class);
    public static final String HEADER = "X-Request-ID";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestId = exchange.getRequest().getHeaders().getFirst(HEADER);
        if (requestId == null || requestId.trim().isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }

        final String trace = requestId;
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(headers -> headers.set(HEADER, trace))
                .build();
        exchange.getResponse().getHeaders().set(HEADER, trace);
        log.info("[{}] gateway -> {}", trace, request.getURI().getPath());
        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
