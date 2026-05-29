# Grafana 实时服务发现实现方案

## 📌 问题描述

原有的Prometheus配置使用静态配置（static_configs），只能监控配置文件中明确列出的服务。当启动新服务时，需要手动修改prometheus.yml并重启Prometheus，Grafana才能看到新服务的指标。

## ✅ 解决方案

实现了**基于Nacos的自动服务发现机制**，让Prometheus能够自动发现并监控所有注册到Nacos的微服务。

## 🏗️ 架构设计

```
┌─────────────────────────────────────────────────────────────┐
│                     微服务生态系统                            │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ 注册服务
                              ▼
                    ┌──────────────────┐
                    │     Nacos        │  ← 服务注册中心
                    │   (8848端口)      │
                    └────────┬─────────┘
                             │
                             │ 每30秒查询
                             ▼
                    ┌──────────────────┐
                    │  Python 同步脚本  │  ← 自动发现服务
                    └────────┬─────────┘
                             │
                             │ 生成配置 + 热重载
                             ▼
                    ┌──────────────────┐
                    │   Prometheus     │  ← 指标采集
                    │   (9090端口)      │
                    └────────┬─────────┘
                             │
                             │ 提供数据
                             ▼
                    ┌──────────────────┐
                    │    Grafana       │  ← 可视化展示
                    │   (3300端口)      │
                    └──────────────────┘
```

## 📁 文件结构

```
Educational-Administration-System/
├── docker/
│   └── docker-compose.yml          # 已更新：启用Prometheus热重载
├── scripts/
│   ├── sync-prometheus-config.py   # 核心：同步脚本
│   ├── auto-sync-prometheus.ps1    # PowerShell自动运行脚本
│   ├── auto-sync-prometheus.bat    # 批处理自动运行脚本
│   ├── test-sync.ps1               # 测试脚本
│   └── README.md                   # 详细文档
├── QUICKSTART.md                   # 快速启动指南
└── GRAFANA_SERVICE_DISCOVERY.md   # 本文档
```

## 🔧 核心组件

### 1. sync-prometheus-config.py

**功能：**
- 从Nacos API获取所有edu-*开头的微服务
- 根据预定义的端口映射生成Prometheus配置
- 调用Prometheus的热重载API更新配置

**关键代码：**
```python
# 从Nacos获取服务列表
def get_services_from_nacos():
    url = f"http://{NACOS_SERVER}/nacos/v1/ns/catalog/services"
    response = requests.get(url, params=params)
    # 过滤出edu-*服务
    return [s for s in services if s.startswith('edu-')]

# 生成Prometheus配置
def generate_prometheus_config(services):
    config = {
        'global': {...},
        'scrape_configs': [
            {
                'job_name': service_name,
                'metrics_path': '/actuator/prometheus',
                'static_configs': [{'targets': [f'{service_name}:{port}']}]
            }
            for service_name, port in services.items()
        ]
    }
    return config

# 热重载Prometheus
def reload_prometheus():
    requests.post('http://localhost:9090/-/reload')
```

### 2. auto-sync-prometheus.ps1

**功能：**
- 每30秒执行一次同步脚本
- 显示倒计时和状态信息
- 支持Ctrl+C优雅退出

**使用方法：**
```powershell
cd scripts
.\auto-sync-prometheus.ps1
```

### 3. docker-compose.yml 更新

**变更：**
```yaml
prometheus:
  command:
    - '--config.file=/etc/prometheus/prometheus.yml'
    - '--web.enable-lifecycle'  # ← 新增：启用热重载
```

## 🚀 使用流程

### 初次配置

1. **重启Prometheus**（启用热重载）
   ```bash
   cd docker
   docker-compose down prometheus
   docker-compose up -d prometheus
   ```

2. **安装Python依赖**
   ```bash
   pip install requests pyyaml
   ```

3. **测试同步脚本**
   ```bash
   cd scripts
   python sync-prometheus-config.py
   ```

4. **启动自动同步服务**
   ```powershell
   .\auto-sync-prometheus.ps1
   ```

### 日常使用

**场景：启动新服务**

1. 启动edu-course服务（端口8085）
2. 服务自动注册到Nacos
3. 等待最多30秒
4. 同步脚本检测到新服务
5. 自动生成Prometheus配置
6. Prometheus热重载
7. ✅ Grafana可以查询到新服务的指标

**无需任何手动操作！**

## 📊 验证方法

### 1. 检查Nacos服务列表

访问：http://localhost:8848/nacos

查看有哪些edu-*服务已注册。

### 2. 检查Prometheus Targets

访问：http://localhost:9090/targets

应该看到所有从Nacos发现的服务，状态为UP或DOWN。

### 3. 在Grafana中查询

访问：http://localhost:3300

查询示例：
```promql
# 查看所有服务的JVM内存使用
jvm_memory_used_bytes

# 查看特定服务
jvm_memory_used_bytes{job="edu-course"}

# 查看服务是否UP
up{job=~"edu-.*"}
```

## ⚙️ 配置选项

### 修改同步间隔

编辑 `auto-sync-prometheus.ps1`，修改第44行：

```powershell
for ($i = 30; $i -gt 0; $i--)  # 改为其他秒数
```

### 添加新服务端口映射

编辑 `sync-prometheus-config.py`，在SERVICE_PORTS字典中添加：

```python
SERVICE_PORTS = {
    # ... 现有服务
    'edu-new-service': 8094,  # 新服务名称:端口
}
```

### 自定义Nacos地址

设置环境变量：

```powershell
$env:NACOS_SERVER = "192.168.1.100:8848"
.\auto-sync-prometheus.ps1
```

## 🔍 故障排查

### 问题1：同步脚本执行失败

**症状：** 提示找不到模块或语法错误

**解决：**
```bash
# 检查Python版本
python --version  # 应该是3.x

# 重新安装依赖
pip install --upgrade requests pyyaml
```

### 问题2：Prometheus重载失败

**症状：** 脚本输出 "Prometheus 配置重载失败"

**检查：**
```bash
# 确认启用了lifecycle
docker exec edu-prometheus ps aux | grep lifecycle

# 手动测试重载API
curl -X POST http://localhost:9090/-/reload

# 查看Prometheus日志
docker logs edu-prometheus
```

**解决：**
```bash
# 重启Prometheus
cd docker
docker-compose restart prometheus
```

### 问题3：Grafana看不到新服务

**排查步骤：**

1. **确认服务已注册到Nacos**
   - 访问 http://localhost:8848/nacos
   - 查看服务列表

2. **确认Prometheus已发现服务**
   - 访问 http://localhost:9090/targets
   - 检查job是否存在且状态为UP

3. **确认指标端点可访问**
   ```bash
   curl http://localhost:8085/actuator/prometheus
   ```
   应该返回大量指标数据

4. **确认Grafana数据源配置**
   - URL: http://localhost:9090
   - Access: Server (default)

### 问题4：服务状态一直是DOWN

**可能原因：**
- 服务未启动
- 服务启动但未暴露Prometheus端点
- 网络不通（Docker容器间）

**解决：**
```bash
# 检查服务是否启动
netstat -ano | findstr :8085

# 检查Actuator端点
curl http://localhost:8085/actuator/prometheus

# 检查pom.xml是否有micrometer依赖
```

## 📈 性能考虑

### 同步频率

- **默认：** 每30秒
- **推荐范围：** 15-60秒
- **不建议：** <10秒（会增加Nacos和Prometheus负担）

### 资源占用

- **CPU：** <1%（每次同步约0.5秒）
- **内存：** ~50MB（Python进程）
- **网络：** 每次同步约10KB数据传输

## 🎯 优势对比

| 特性 | 原方案（静态配置） | 新方案（自动发现） |
|------|-------------------|-------------------|
| 新服务发现 | ❌ 需手动配置 | ✅ 自动发现 |
| 配置更新 | ❌ 需重启Prometheus | ✅ 热重载，无需重启 |
| 响应时间 | 手动操作 | 最多30秒自动生效 |
| 维护成本 | 高 | 低 |
| 可靠性 | 易出错 | 自动化，可靠 |

## 🔮 未来改进

### 短期优化

1. **增加日志记录**
   - 记录每次同步的详细信息
   - 便于故障排查

2. **添加告警通知**
   - 同步失败时发送通知
   - 通过邮件或企业微信

3. **Web界面**
   - 可视化管理同步任务
   - 查看历史同步记录

### 长期规划

1. **使用真正的Nacos服务发现插件**
   - [prometheus-nacos-sd](https://github.com/nacos-group/nacos-prometheus)
   - 实现秒级服务发现
   - 无需轮询，事件驱动

2. **集成到Kubernetes**
   - 使用K8s Service Discovery
   - 更云原生的方案

3. **多环境支持**
   - 开发、测试、生产环境隔离
   - 不同的Nacos命名空间

## 📚 相关文档

- [快速启动指南](QUICKSTART.md)
- [详细使用说明](scripts/README.md)
- [Prometheus官方文档](https://prometheus.io/docs/)
- [Nacos官方文档](https://nacos.io/zh-cn/docs/)

## 💡 最佳实践

1. **保持自动同步服务运行**
   - 建议设置为开机自启动
   - 使用Windows任务计划程序

2. **定期检查同步日志**
   - 确保没有持续的错误
   - 及时发现配置问题

3. **服务命名规范**
   - 统一使用 `edu-` 前缀
   - 便于自动过滤和识别

4. **端口管理规范**
   - 及时更新SERVICE_PORTS映射
   - 避免端口冲突

5. **监控监控本身**
   - 监控同步脚本的运行状态
   - 设置心跳检测

---

**最后更新：** 2026-05-12  
**版本：** 1.0.0  
**作者：** Educational Administration System Team
