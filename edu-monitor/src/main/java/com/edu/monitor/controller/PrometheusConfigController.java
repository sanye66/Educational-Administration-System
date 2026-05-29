package com.edu.monitor.controller;

import com.edu.common.core.result.Result;
import com.edu.monitor.service.PrometheusConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Prometheus配置管理控制器
 */
@RestController
@RequestMapping("/api/prometheus")
@RequiredArgsConstructor
@Slf4j
public class PrometheusConfigController {

    private final PrometheusConfigService prometheusConfigService;

    /**
     * 手动触发Prometheus配置更新
     */
    @PostMapping("/update")
    public Result<String> updateConfig() {
        log.info("收到手动更新Prometheus配置请求");
        boolean success = prometheusConfigService.updatePrometheusConfig();
        if (success) {
            return Result.success("Prometheus配置更新成功");
        } else {
            return Result.error("Prometheus配置更新失败");
        }
    }

    /**
     * 获取当前服务列表
     */
    @GetMapping("/services")
    public Result<?> getServices() {
        try {
            var services = prometheusConfigService.getEduServices();
            return Result.success(services);
        } catch (Exception e) {
            log.error("获取服务列表失败", e);
            return Result.error("获取服务列表失败: " + e.getMessage());
        }
    }
}
