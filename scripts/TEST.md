# Prometheus 自动服务发现 - 快速测试

## 🧪 快速测试流程

### 测试1：手动同步

```powershell
cd C:\Users\Lenovo\Desktop\Educational-Administration-System\scripts
python sync-prometheus-config.py
```

**预期结果：**
```
============================================================
Prometheus Nacos 服务发现同步工具
============================================================

正在从 Nacos 获取服务列表...
发现 X 个服务: edu-gateway, edu-auth, ...

正在生成 Prometheus 配置...
配置文件已更新: ./config/prometheus.yml

正在重载 Prometheus...
Prometheus 配置重载成功

✅ 同步完成!
============================================================
```

### 测试2：检查生成的配置文件

查看 `docker/config/prometheus.yml`，应该包含所有从Nacos发现的服务：

```yaml
global:
  scrape_interval: 15s
  evaluation_interval: 15s
scrape_configs:
- job_name: edu-gateway
  metrics_path: /actuator/prometheus
  static_configs:
  - targets:
    - edu-gateway:8080
- job_name: edu-auth
  metrics_path: /actuator/prometheus
  static_configs:
  - targets:
    - edu-auth:8081
# ... 更多服务
```

### 测试3：访问Prometheus Targets

浏览器打开：http://localhost:9090/targets

**应该看到：**
- 所有edu-*服务的job
- 状态为UP（如果服务已启动）或DOWN（如果服务未启动）

### 测试4：启动新服务并观察自动发现

1. **记录当前状态**
   - 访问 http://localhost:9090/targets
   - 截图或记录当前的服务列表

2. **启动一个新服务**（如edu-course）
   ```bash
   cd edu-services/edu-course
   mvn spring-boot:run
   ```

3. **等待30秒**（或运行手动同步）
   ```powershell
   python sync-prometheus-config.py
   ```

4. **刷新Prometheus页面**
   - 访问 http://localhost:9090/targets
   - 应该看到新增的 `edu-course` job

5. **在Grafana中查询**
   - 访问 http://localhost:3300
   - 进入Explore页面
   - 查询：`jvm_memory_used_bytes{job="edu-course"}`
   - 应该能看到数据图表

## ✅ 测试检查清单

- [ ] Python依赖已安装（requests, pyyaml）
- [ ] Prometheus已重启并启用lifecycle
- [ ] 手动同步脚本执行成功
- [ ] prometheus.yml文件已正确生成
- [ ] Prometheus Targets页面显示所有服务
- [ ] 启动新服务后能自动发现
- [ ] Grafana能查询到新服务的指标

## ❌ 常见问题速查

### 问题：提示 "ModuleNotFoundError: No module named 'requests'"

**解决：**
```bash
pip install requests pyyaml
```

### 问题：Prometheus重载失败

**检查：**
```bash
docker logs edu-prometheus | grep lifecycle
```

应该看到 `web.enable-lifecycle=true`

如果没有，重启Prometheus：
```bash
cd docker
docker-compose restart prometheus
```

### 问题：Nacos连接失败

**检查Nacos是否运行：**
```bash
curl http://localhost:8848/nacos/v1/console/health/liveness
```

应该返回 `OK`

### 问题：Grafana看不到数据

**排查步骤：**
1. 确认Prometheus targets中服务状态为UP
2. 确认服务暴露了 `/actuator/prometheus` 端点
3. 确认Grafana数据源配置正确（URL: http://localhost:9090）

**测试端点：**
```bash
curl http://localhost:8085/actuator/prometheus | head -n 20
```

应该返回指标数据。

## 🎯 下一步

测试通过后：

1. **启动自动同步服务**
   ```powershell
   .\auto-sync-prometheus.ps1
   ```

2. **保持窗口运行**
   - 不要关闭这个PowerShell窗口
   - 它会每30秒自动同步一次

3. **正常使用系统**
   - 启动任何新服务都会自动被监控
   - 无需手动配置Prometheus
   - Grafana实时显示所有服务指标

## 📞 需要帮助？

查看详细文档：
- [快速启动指南](../QUICKSTART.md)
- [详细说明](README.md)
- [架构设计](../GRAFANA_SERVICE_DISCOVERY.md)
