package com.edu.gateway.filter;

import com.edu.common.core.constant.SystemConstants;
import com.edu.common.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * 认证全局过滤器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * 白名单路径，不需要认证
     */
    private static final List<String> WHITE_LIST = Arrays.asList(
            "/auth/login",
            "/auth/register",
            "/auth/captcha",
            "/auth/refresh",
            "/actuator/**",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 临时禁用:直接放行所有请求
        log.info("AuthGlobalFilter 已禁用,放行所有请求");
        return chain.filter(exchange);
        
        /* 原有逻辑
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        log.info("AuthGlobalFilter 拦截请求: {}", path);

        // 白名单放行
        if (isWhiteListed(path)) {
            log.info("白名单路径,放行: {}", path);
            return chain.filter(exchange);
        }

        log.warn("非白名单路径,需要认证: {}", path);

        // 获取Token
        String token = getToken(request);
        if (token == null) {
            log.warn("请求未携带Token: {}", path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            // 验证Token
            if (jwtUtil.isTokenExpired(token)) {
                log.warn("Token已过期: {}", path);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String username = jwtUtil.getUsernameFromToken(token);
            Long userId = jwtUtil.getUserIdFromToken(token);

            // 将用户信息传递到下游服务
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header(SystemConstants.USER_ID_HEADER, String.valueOf(userId))
                    .header(SystemConstants.USERNAME_HEADER, username)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        } catch (Exception e) {
            log.error("Token验证失败: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        */
    }

    private boolean isWhiteListed(String path) {
        return WHITE_LIST.stream().anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
    }

    private String getToken(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String authorization = headers.getFirst(SystemConstants.TOKEN_HEADER);
        if (authorization != null && authorization.startsWith(SystemConstants.TOKEN_PREFIX)) {
            return authorization.substring(SystemConstants.TOKEN_PREFIX.length());
        }
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
