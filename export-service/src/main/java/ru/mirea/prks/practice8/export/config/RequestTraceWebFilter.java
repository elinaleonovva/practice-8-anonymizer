package ru.mirea.prks.practice8.export.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

/** WebFlux correlation filter used by both the API and an SSE subscription. */
@Component
public class RequestTraceWebFilter implements WebFilter {
    private static final Logger log = LoggerFactory.getLogger(RequestTraceWebFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String requestId = exchange.getRequest().getHeaders().getFirst("X-Request-ID");
        if (requestId == null || requestId.trim().isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }
        final String trace = requestId;
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(headers -> headers.set("X-Request-ID", trace))
                .build();
        exchange.getResponse().getHeaders().set("X-Request-ID", trace);
        log.info("[{}] export-service {} {}", trace, request.getMethod(), request.getURI().getPath());
        return chain.filter(exchange.mutate().request(request).build());
    }
}
