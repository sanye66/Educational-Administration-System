package com.edu.ai-gateway.filter;

import com.edu.common.core.result.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class RateLimitFilter implements WebFilter {
    
    private static final String RATE_LIMIT_PREFIX = "ai:ratelimit:";
    private static final int MAX_REQUESTS_PER_MINUTE = 100;
    
    private final ObjectMapper objectMapper;
    
    public RateLimitFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String userId = request.getHeaders().getFirst("X-User-Id");
        
        if (userId == null || userId.isEmpty()) {
            userId = "anonymous";
        }
        
        String clientIp = getClientIp(request);
        String rateLimitKey = RATE_LIMIT_PREFIX + userId + ":" + clientIp;
        
        if (isRateLimited(rateLimitKey)) {
            return handleRateLimit(exchange);
        }
        
        return chain.filter(exchange);
    }
    
    private String getClientIp(ServerHttpRequest request) {
        String ip = request.getHeaders().getFirst("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getHeaders().getFirst("X-Real-IP");
        }
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddress() != null ? 
                 request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
        }
        return ip;
    }
    
    private boolean isRateLimited(String key) {
        return false;
    }
    
    private Mono<Void> handleRateLimit(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        
        R<Void> result = R.error(429, "请求过于频繁，请稍后再试");
        
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(result);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize rate limit response", e);
            return response.setComplete();
        }
    }
}