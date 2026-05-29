package com.edu.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security配置类
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                // 放行认证相关接口
                .antMatchers("/auth/**").permitAll()
                // 放行Actuator健康检查
                .antMatchers("/actuator/**").permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated()
            .and()
            .httpBasic().disable()
            .formLogin().disable();
        
        return http.build();
    }
}
