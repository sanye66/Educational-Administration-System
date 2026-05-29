package com.edu.monitor.service;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Prometheus配置管理服务
 * 负责从Nacos获取服务列表并动态更新Prometheus配置
 */
@Service
@Slf4j
public class PrometheusConfigService {

    @Value("${nacos.server-addr:localhost:8848}")
    private String nacosServerAddr;

    @Value("${prometheus.config.path:./config/prometheus.yml}")
    private String prometheusConfigPath;

    @Value("${prometheus.reload.url:http://localhost:9090/-/reload}")
    private String prometheusReloadUrl;

    private NamingService namingService;

    // 已知服务的端口映射
    private static final Map<String, Integer> SERVICE_PORTS = new HashMap<>();

    static {
        SERVICE_PORTS.put("edu-gateway", 8080);
        SERVICE_PORTS.put("edu-auth", 8081);
        SERVICE_PORTS.put("edu-user", 8082);
        SERVICE_PORTS.put("edu-teacher", 8083);
        SERVICE_PORTS.put("edu-student", 8084);
        SERVICE_PORTS.put("edu-course", 8085);
        SERVICE_PORTS.put("edu-classroom", 8086);
        SERVICE_PORTS.put("edu-selection", 8087);
        SERVICE_PORTS.put("edu-grade", 8088);
        SERVICE_PORTS.put("edu-exam", 8089);
        SERVICE_PORTS.put("edu-schedule", 8090);
        SERVICE_PORTS.put("edu-graduation", 8091);
        SERVICE_PORTS.put("edu-evaluation", 8092);
        SERVICE_PORTS.put("edu-monitor", 8093);
    }

    @PostConstruct
    public void init() throws NacosException {
        Properties properties = new Properties();
        properties.put("serverAddr", nacosServerAddr);
        namingService = NamingFactory.createNamingService(properties);
        log.info("Prometheus配置服务初始化完成，Nacos地址: {}", nacosServerAddr);
    }

    /**
     * 从Nacos获取所有edu-开头的服务
     */
    public List<String> getEduServices() {
        try {
            List<String> services = namingService.getServicesOfServer(1, 100).getData();
            return services.stream()
                    .filter(service -> service.startsWith("edu-"))
                    .collect(Collectors.toList());
        } catch (NacosException e) {
            log.error("从Nacos获取服务列表失败", e);
            return new ArrayList<>(SERVICE_PORTS.keySet());
        }
    }

    /**
     * 生成Prometheus配置内容
     */
    public String generatePrometheusConfig(List<String> services) {
        StringBuilder config = new StringBuilder();
        config.append("global:\n");
        config.append("  scrape_interval: 15s\n");
        config.append("  evaluation_interval: 15s\n\n");
        config.append("scrape_configs:\n");

        for (String serviceName : services) {
            Integer port = SERVICE_PORTS.get(serviceName);
            if (port != null) {
                config.append("  - job_name: '").append(serviceName).append("'\n");
                config.append("    metrics_path: '/actuator/prometheus'\n");
                config.append("    static_configs:\n");
                config.append("      - targets: ['").append(serviceName).append(":").append(port).append("']\n\n");
            } else {
                log.warn("未找到服务 {} 的端口配置", serviceName);
            }
        }

        return config.toString();
    }

    /**
     * 更新Prometheus配置文件
     */
    public boolean updatePrometheusConfig() {
        try {
            List<String> services = getEduServices();
            log.info("发现 {} 个教育服务: {}", services.size(), services);

            String configContent = generatePrometheusConfig(services);
            
            // 确保目录存在
            Path configPath = Paths.get(prometheusConfigPath);
            Files.createDirectories(configPath.getParent());

            // 写入配置文件
            try (FileWriter writer = new FileWriter(configPath.toFile())) {
                writer.write(configContent);
            }

            log.info("Prometheus配置文件已更新: {}", prometheusConfigPath);
            
            // 触发Prometheus重载
            reloadPrometheus();
            
            return true;
        } catch (IOException e) {
            log.error("更新Prometheus配置文件失败", e);
            return false;
        }
    }

    /**
     * 触发Prometheus配置重载
     */
    private void reloadPrometheus() {
        try {
            java.net.URL url = new java.net.URL(prometheusReloadUrl);
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                log.info("Prometheus配置重载成功");
            } else {
                log.warn("Prometheus配置重载失败，HTTP状态码: {}", responseCode);
            }
            
            connection.disconnect();
        } catch (Exception e) {
            log.error("重载Prometheus配置失败", e);
        }
    }

    /**
     * 监听服务变化并更新配置
     */
    public void watchServiceChanges() {
        try {
            List<String> services = getEduServices();
            for (String serviceName : services) {
                namingService.subscribe(serviceName, event -> {
                    log.info("检测到服务 {} 发生变化，更新Prometheus配置", serviceName);
                    updatePrometheusConfig();
                });
            }
        } catch (NacosException e) {
            log.error("订阅服务变化失败", e);
        }
    }
}