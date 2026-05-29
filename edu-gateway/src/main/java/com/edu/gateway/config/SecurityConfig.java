package com.edu.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * 网关Security配置 - 放行所有请求
 * 注意:生产环境应该启用认证,由AuthGlobalFilter处理
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
            .csrf().disable()
            .httpBasic().disable()
            .formLogin().disable()
            .authorizeExchange()
                .anyExchange().permitAll()
            .and()
            .build();
    }
}
