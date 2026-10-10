package ru.mirea.prks.practice8.dataset.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/** Also guarantees a correlation identifier for a direct local service call. */
@Component
public class RequestTraceFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(RequestTraceFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestId = request.getHeader("X-Request-ID");
        if (requestId == null || requestId.trim().isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }
        response.setHeader("X-Request-ID", requestId);
        log.info("[{}] dataset-service {} {}", requestId, request.getMethod(), request.getRequestURI());
        filterChain.doFilter(request, response);
    }
}
