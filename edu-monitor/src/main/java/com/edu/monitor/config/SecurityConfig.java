package com.edu.monitor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * 监控服务安全配置
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 启用CORS
            .cors(cors -> {})
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(new AntPathRequestMatcher("/actuator/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/**")).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(withDefaults())
            .csrf(csrf -> csrf.ignoringRequestMatchers(
                new AntPathRequestMatcher("/actuator/**"),
                new AntPathRequestMatcher("/instances")
            ));
        return http.build();
    }
}
