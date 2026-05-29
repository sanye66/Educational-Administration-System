# Prometheus 自动服务发现

## 概述

本方案实现了Prometheus从Nacos自动发现微服务，无需手动配置每个服务。

## 工作原理

```
┌─────────────┐      获取服务列表      ┌──────────────┐
│             │ ────────────────────> │              │
│   Nacos     │                       │  Python 脚本  │
│             │ <──────────────────── │              │
└─────────────┘   返回edu-*服务        └──────┬───────┘
                                             │
                                             │ 生成配置
                                             ▼
                                      ┌──────────────┐
                                      │ prometheus.  │
                                      │    yml       │
                                      └──────┬───────┘
                                             │
                                             │ 热重载
                                             ▼
                                      ┌──────────────┐
                                      │  Prometheus  │
                                      └──────┬───────┘
                                             │
                                             │ 采集指标
                                             ▼
                                      ┌──────────────┐
                                      │   Grafana    │
                                      └──────────────┘
```

## 文件说明

- `sync-prometheus-config.py` - 主同步脚本
- `auto-sync-prometheus.bat` - Windows自动同步批处理（每30秒执行）
- `test-sync.ps1` - PowerShell测试脚本

## 使用方法

### 方法1：手动同步（一次性）

```bash
cd scripts
python sync-prometheus-config.py
```

### 方法2：自动同步（推荐）

启动自动同步服务，它会每30秒检查一次Nacos并更新配置：

```bash
cd scripts
.\auto-sync-prometheus.bat
```

**保持这个窗口运行**，它会在后台持续监控服务变化。

### 方法3：使用Windows任务计划程序

可以配置为开机自动启动，作为后台服务运行。

## 前提条件

### 1. 安装Python依赖

```bash
pip install requests pyyaml
```

### 2. 确保Prometheus启用热重载

Docker启动时需要添加参数：

```yaml
# docker-compose.yml
prometheus:
  image: prom/prometheus:latest
  container_name: edu-prometheus
  ports:
    - "9090:9090"
  volumes:
    - ./config/prometheus.yml:/etc/prometheus/prometheus.yml
  command:
    - '--config.file=/etc/prometheus/prometheus.yml'
    - '--web.enable-lifecycle'  # 启用热重载
  networks:
    - edu-network
```

**重要**: 必须添加 `'--web.enable-lifecycle'` 参数！

### 3. 重启Prometheus容器

```bash
docker-compose down prometheus
docker-compose up -d prometheus
```

## 验证

### 1. 测试同步脚本

```bash
cd scripts
python sync-prometheus-config.py
```

应该看到类似输出：
```
============================================================
Prometheus Nacos 服务发现同步工具
============================================================

正在从 Nacos 获取服务列表...
发现 5 个服务: edu-gateway, edu-auth, edu-user, edu-teacher, edu-student

正在生成 Prometheus 配置...
配置文件已更新: ./config/prometheus.yml

正在重载 Prometheus...
Prometheus 配置重载成功

✅ 同步完成!
============================================================
```

### 2. 检查Prometheus Targets

访问 http://localhost:9090/targets

应该看到所有从Nacos发现的服务，状态为UP或DOWN。

### 3. 检查Grafana

在Grafana中查询指标，例如：
```
jvm_memory_used_bytes{job="edu-course"}
```

如果启动了edu-course服务，应该能看到数据。

## 工作流程

1. **启动新服务**（如edu-course）
2. 服务注册到Nacos
3. 同步脚本检测到新服务（最多30秒）
4. 自动生成Prometheus配置
5. 热重载Prometheus
6. Prometheus开始采集新服务的指标
7. Grafana可以查询到新服务的指标

## 故障排查

### 问题1：脚本执行失败

**检查Python是否安装**
```bash
python --version
```

**检查依赖是否安装**
```bash
pip list | findstr requests
pip list | findstr PyYAML
```

### 问题2：Prometheus重载失败

**确认启用了lifecycle**
```bash
docker exec edu-prometheus ps aux | grep lifecycle
```

**手动测试重载API**
```bash
curl -X POST http://localhost:9090/-/reload
```

### 问题3：Grafana看不到新服务

**检查Prometheus targets**
- 访问 http://localhost:9090/targets
- 确认新服务的job状态为UP

**检查数据采集**
- 在Prometheus的Graph页面查询：`up{job="edu-course"}`
- 应该返回 1（UP）或 0（DOWN）

**检查Grafana数据源**
- 确认Grafana的Prometheus数据源URL正确
- 默认应该是 http://localhost:9090

## 高级配置

### 自定义同步间隔

编辑 `auto-sync-prometheus.bat`，修改第15行的超时时间：

```batch
timeout /t 30 /nobreak >nul  # 改为其他秒数
```

### 添加新服务端口映射

编辑 `sync-prometheus-config.py`，在SERVICE_PORTS字典中添加：

```python
SERVICE_PORTS = {
    # ... 现有服务
    'edu-new-service': 8094,  # 新服务
}
```

### 自定义Nacos地址

设置环境变量：

```bash
set NACOS_SERVER=192.168.1.100:8848
python sync-prometheus-config.py
```

## 注意事项

⚠️ **重要提示**

1. Prometheus必须启用 `--web.enable-lifecycle` 参数
2. 同步脚本需要持续运行才能实时发现服务
3. 新服务启动后，最多需要30秒才能在Grafana中看到
4. 确保Prometheus容器挂载了正确的配置文件路径
5. Docker环境中的服务名需要使用容器网络名称

## 未来改进

可以考虑使用真正的Nacos服务发现插件：
- [prometheus-nacos-sd](https://github.com/nacos-group/nacos-prometheus)
- 这样可以实现秒级服务发现，无需轮询
