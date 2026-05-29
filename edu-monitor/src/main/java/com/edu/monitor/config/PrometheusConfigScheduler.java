package com.edu.monitor.config;

import com.edu.monitor.service.PrometheusConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class PrometheusConfigScheduler {

    private final PrometheusConfigService prometheusConfigService;

    /**
     * 应用启动完成后立即更新一次配置
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("应用启动完成，开始初始化Prometheus配置");
        prometheusConfigService.updatePrometheusConfig();
        prometheusConfigService.watchServiceChanges();
    }

    /**
     * 每30秒检查一次服务列表变化（作为订阅机制的补充）
     */
    @Scheduled(fixedRate = 30000)
    public void scheduledUpdate() {
        log.debug("定时检查服务列表变化");
        prometheusConfigService.updatePrometheusConfig();
    }
}