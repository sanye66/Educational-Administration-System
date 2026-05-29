# Prometheus 自动服务发现 - 快速启动指南

## 🚀 5分钟快速配置

### 步骤1：重启Prometheus（启用热重载）

```bash
cd docker
docker-compose down prometheus
docker-compose up -d prometheus
```

验证是否成功：
```bash
docker logs edu-prometheus
```

应该看到类似输出，包含 `web.enable-lifecycle=true`

### 步骤2：安装Python依赖

```bash
pip install requests pyyaml
```

或者如果使用的是Python3：
```bash
pip3 install requests pyyaml
```

### 步骤3：测试同步脚本

```bash
cd scripts
python sync-prometheus-config.py
```

**预期输出：**
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

### 步骤4：启动自动同步服务

**方式A：手动启动（推荐用于测试）**

打开一个新的PowerShell窗口：
```powershell
cd C:\Users\Lenovo\Desktop\Educational-Administration-System\scripts
.\auto-sync-prometheus.bat
```

**保持这个窗口运行！**它会每30秒自动同步一次。

**方式B：后台运行（高级）**

创建Windows任务计划程序任务，开机自动启动。

### 步骤5：验证

#### 5.1 检查Prometheus Targets

浏览器访问：http://localhost:9090/targets

应该看到所有从Nacos发现的服务：
- ✅ edu-gateway (UP)
- ✅ edu-auth (UP)
- ⬇️ edu-user (DOWN) - 如果未启动
- ...

#### 5.2 测试新服务发现

1. **启动一个新服务**（如edu-course）
2. **等待最多30秒**
3. **刷新** http://localhost:9090/targets
4. 应该看到 `edu-course` 出现在列表中

#### 5.3 在Grafana中查询

1. 访问 http://localhost:3300 （Grafana）
2. 登录：admin / admin
3. 进入 Explore 页面
4. 查询指标：
   ```
   jvm_memory_used_bytes{job="edu-course"}
   ```
5. 如果服务已启动，应该能看到数据图表

## 📋 常用命令

### 查看当前有哪些服务在Nacos中

```bash
curl http://localhost:8848/nacos/v1/ns/catalog/services?pageNo=1&pageSize=100
```

### 手动触发一次同步

```bash
cd scripts
python sync-prometheus-config.py
```

### 查看Prometheus日志

```bash
docker logs -f edu-prometheus
```

### 重启自动同步服务

关闭当前的bat窗口，重新运行：
```bash
.\auto-sync-prometheus.bat
```

## ❓ 常见问题

### Q1: 提示 "找不到模块 requests"

**解决方案：**
```bash
pip install requests pyyaml
```

如果还是不行，尝试：
```bash
python -m pip install requests pyyaml
```

### Q2: Prometheus重载失败

**检查是否启用了lifecycle：**
```bash
docker exec edu-prometheus cat /etc/prometheus/prometheus.yml
```

**重启Prometheus：**
```bash
cd docker
docker-compose restart prometheus
```

### Q3: Grafana看不到数据

**检查清单：**
1. ✅ Prometheus targets中服务状态是否为UP？
2. ✅ 服务是否正确暴露了 `/actuator/prometheus` 端点？
3. ✅ Grafana数据源配置是否正确？（URL: http://localhost:9090）
4. ✅ 查询的指标名称是否正确？

**测试指标采集：**
```bash
curl http://localhost:8085/actuator/prometheus
```
应该返回大量指标数据。

### Q4: 新服务没有自动出现

**排查步骤：**
1. 确认服务已注册到Nacos：http://localhost:8848/nacos
2. 检查自动同步脚本是否在运行
3. 手动执行一次同步：`python sync-prometheus-config.py`
4. 查看脚本输出是否有错误
5. 检查Prometheus targets页面

## 🎯 下一步

配置完成后，您可以：

1. **导入Grafana仪表板**
   - 使用JVM监控模板
   - 使用Spring Boot监控模板

2. **配置告警规则**
   - CPU使用率过高
   - 内存泄漏检测
   - 服务宕机告警

3. **优化同步间隔**
   - 编辑 `auto-sync-prometheus.bat`
   - 修改 `timeout /t 30` 为其他值

## 📞 需要帮助？

查看详细文档：`scripts/README.md`
