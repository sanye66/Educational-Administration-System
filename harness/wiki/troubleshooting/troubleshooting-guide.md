# 故障排除指南

本文档提供教学管理系统常见故障的排查与解决方法。

## 1. 服务启动失败

### 1.1 端口被占用

**症状**：服务启动失败，提示端口已被占用

**排查步骤**：

```bash
# 1. 查看端口占用情况
netstat -tlnp | grep <端口号>

# 例如检查 8080 端口
netstat -tlnp | grep 8080
```

**解决方案**：

```bash
# 杀掉占用端口的进程
kill -9 <PID>

# 或者修改服务端口配置
# 在 application.yml 中修改 server: port
```

### 1.2 数据库连接失败

**症状**：服务启动失败，提示无法连接数据库

**排查步骤**：

```bash
# 1. 检查 MySQL 服务状态
systemctl status mysqld

# 2. 测试数据库连接
mysql -h localhost -u root -p -e "SELECT 1"

# 3. 检查数据库是否存在
mysql -u root -p -e "SHOW DATABASES"
```

**解决方案**：

1. 启动 MySQL 服务
2. 创建对应的数据库
3. 检查 application.yml 中的数据库配置

### 1.3 Nacos 连接失败

**症状**：服务无法注册到 Nacos，控制台报错

**排查步骤**：

```bash
# 1. 检查 Nacos 是否启动
curl http://localhost:8848/nacos

# 2. 检查 Nacos 日志
tail -f nacos/logs/start.out
```

**解决方案**：

```yaml
# 检查配置
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
```

## 2. 接口调用失败

### 2.1 404 Not Found

**症状**：请求接口返回 404

**排查步骤**：

1. 检查服务是否启动
2. 检查网关路由配置
3. 检查接口路径是否正确

**解决方案**：

```yaml
# 检查网关路由配置
spring:
  cloud:
    gateway:
      routes:
        - id: edu-user
          uri: lb://edu-user
          predicates:
            - Path=/api/user/**
```

### 2.2 500 Internal Server Error

**症状**：请求返回 500 错误

**排查步骤**：

```bash
# 查看服务日志
kubectl logs <pod-name> -n education

# 查看异常堆栈
```

**常见原因**：

1. 空指针异常
2. 数据库操作异常
3. 业务逻辑异常

**解决方案**：

根据日志定位问题，修复代码或数据

### 2.3 503 Service Unavailable

**症状**：服务暂时不可用

**排查步骤**：

```bash
# 1. 检查服务状态
kubectl get pods -n education

# 2. 检查服务健康状态
curl http://<service>/actuator/health
```

**解决方案**：

```bash
# 重启服务
kubectl rollout restart deployment/<service> -n education
```

## 3. 认证授权问题

### 3.1 Token 过期

**症状**：提示登录已过期

**解决方案**：

1. 前端重新登录获取新 Token
2. 检查 Token 过期时间配置

### 3.2 Token 无效

**症状**：提示无效令牌

**排查步骤**：

1. 检查 Token 是否正确传递
2. 检查 Token 是否被篡改
3. 检查 JWT 密钥配置

### 3.3 无权限访问

**症状**：提示无权限访问

**解决方案**：

```java
// 检查接口权限注解
@PreAuthorize("hasAuthority('xxx')")
public void method() { }
```

## 4. 数据库问题

### 4.1 连接数过多

**症状**：无法获取数据库连接

**排查步骤**：

```sql
-- 查看当前连接数
SHOW STATUS LIKE 'Threads_connected';

-- 查看最大连接数
SHOW VARIABLES LIKE 'max_connections';

-- 查看当前连接
SHOW PROCESSLIST;
```

**解决方案**：

```yaml
# 配置连接池
spring:
  datasource:
    druid:
      max-active: 20
```

### 4.2 慢查询

**症状**：接口响应慢

**排查步骤**：

```sql
-- 开启慢查询日志
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 1;

-- 查看慢查询
SELECT * FROM mysql.slow_log ORDER BY start_time DESC LIMIT 10;
```

**解决方案**：

1. 添加索引
2. 优化 SQL
3. 分页查询

### 4.3 死锁

**症状**：操作超时

**排查步骤**：

```sql
-- 查看正在执行的事务
SELECT * FROM information_schema.INNODB_TRX;

-- 查看锁等待
SELECT * FROM information_schema.INNODB_LOCK_WAITS;
```

**解决方案**：

1. 调整事务顺序
2. 减小事务范围
3. 使用乐观锁

## 5. Redis 问题

### 5.1 连接失败

**症状**：无法连接 Redis

**排查步骤**：

```bash
# 测试 Redis 连接
redis-cli ping

# 查看 Redis 日志
tail -f /var/log/redis/redis.log
```

**解决方案**：

1. 检查 Redis 服务状态
2. 检查防火墙配置
3. 检查连接配置

### 5.2 缓存穿透

**症状**：大量请求直接访问数据库

**解决方案**：

1. 使用布隆过滤器
2. 缓存空值

### 5.3 缓存雪崩

**症状**：大量缓存同时失效

**解决方案**：

1. 随机过期时间
2. 永不过期 + 定时更新
3. 逻辑过期

## 6. 前端问题

### 6.1 请求跨域

**症状**：浏览器报 CORS 错误

**解决方案**：

```yaml
# 配置 CORS
spring:
  cloud:
    gateway:
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins: "*"
            allowedMethods: "*"
            allowedHeaders: "*"
```

### 6.2 请求失败

**症状**：网络请求失败

**排查步骤**：

1. 检查浏览器控制台
2. 检查 Network 面板
3. 检查后端服务日志

## 7. 性能问题

### 7.1 CPU 使用率高

**排查步骤**：

```bash
# 查看 CPU 使用情况
top

# 查看 Java 进程
jps

# 导出线程堆栈
jstack <pid> > thread.log
```

### 7.2 内存使用率高

**排查步骤**：

```bash
# 查看内存使用
jmap -heap <pid>

# 导出堆转储
jmap -dump:format=b,file=heap.hprof <pid>
```

### 7.3 接口响应慢

**排查步骤**：

1. 检查数据库慢查询
2. 检查 Redis 缓存命中率
3. 检查服务线程池配置
4. 检查网络延迟

## 8. 紧急故障处理

### 8.1 服务完全不可用

1. 检查基础设施（网络、电源）
2. 检查 Kubernetes 集群状态
3. 检查日志定位问题
4. 回滚到上一版本

### 8.2 数据丢失

1. 立即停止写入操作
2. 评估数据丢失范围
3. 从备份恢复数据
4. 分析原因并修复

### 8.3 安全事件

1. 隔离受影响系统
2. 评估影响范围
3. 修复漏洞
4. 上报安全事件
