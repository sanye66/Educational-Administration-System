# 运维手册

本文档提供教学管理系统的日常运维操作指南。

## 1. 服务管理

### 1.1 服务启停

#### 单机部署（Docker Compose）

```bash
# 启动所有服务
cd docker
docker-compose up -d

# 停止所有服务
docker-compose down

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f edu-gateway
```

#### Kubernetes 部署

```bash
# 查看所有 Pod
kubectl get pods -n education

# 查看服务日志
kubectl logs -f deployment/edu-gateway -n education

# 重启服务
kubectl rollout restart deployment/edu-user -n education

# 扩缩容
kubectl scale deployment/edu-user --replicas=3 -n education
```

### 1.2 服务健康检查

```bash
# 检查服务健康状态
curl http://localhost:8080/actuator/health

# 检查各个服务
for port in 8081 8082 8083 8084 8085; do
  echo "Checking port $port..."
  curl -s http://localhost:$port/actuator/health || echo "Failed"
done
```

### 1.3 服务依赖关系

```
edu-gateway (8080)
    ↓
edu-auth (8081) ← Redis / MySQL
edu-monitor (8094)
    ↓
业务服务 (8082-8093)
    ↓
MySQL / Redis / Nacos / Seata
```

## 2. 数据库运维

### 2.1 数据库备份

```bash
# 备份单个数据库
mysqldump -u root -p edu_user > edu_user_$(date +%Y%m%d).sql

# 备份所有数据库
mysqldump -u root -p --all-databases > all_db_$(date +%Y%m%d).sql

# 定时备份（crontab）
0 2 * * * /path/to/backup.sh
```

### 2.2 数据库恢复

```bash
# 恢复单个数据库
mysql -u root -p edu_user < edu_user_20240101.sql
```

### 2.3 常见数据库操作

```sql
-- 查看数据库大小
SELECT table_schema AS 'Database',
       ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS 'Size (MB)'
FROM information_schema.tables
GROUP BY table_schema;

-- 查看慢查询
SHOW VARIABLES LIKE 'slow_query_log%';
SELECT * FROM mysql.slow_log ORDER BY start_time DESC LIMIT 10;
```

## 3. 缓存运维

### 3.1 Redis 操作

```bash
# 连接 Redis
redis-cli -h localhost -p 6379

# 查看内存使用
INFO memory

# 查看 Key 数量
DBSIZE

# 清除所有缓存（慎用）
FLUSHALL

# 清除当前数据库缓存
FLUSHDB
```

### 3.2 缓存预热

```bash
# 预热用户缓存
curl -X POST http://localhost:8082/api/user/cache/warmup
```

## 4. 日志管理

### 4.1 日志查看

```bash
# 查看实时日志
kubectl logs -f deployment/edu-user -n education

# 查看历史日志
kubectl logs --since=1h deployment/edu-user -n education

# 查看错误日志
kubectl logs deployment/edu-user -n education | grep ERROR
```

### 4.2 日志级别调整

```yaml
# application.yml
logging:
  level:
    root: INFO
    com.edu: DEBUG
    org.springframework: WARN
```

## 5. 监控运维

### 5.1 查看监控指标

- Spring Boot Admin: http://monitor-server:8080
- Prometheus: http://prometheus:9090
- Grafana: http://grafana:3000

### 5.2 告警处理

| 告警级别 | 说明 | 处理方式 |
|----------|------|----------|
| Critical | 服务不可用 | 立即处理，检查服务状态 |
| Warning | 性能下降 | 30 分钟内处理 |
| Info | 一般信息 | 记录观察 |

## 6. 日常检查清单

### 6.1 每日检查

- [ ] 检查所有服务是否正常运行
- [ ] 检查数据库连接是否正常
- [ ] 检查磁盘空间是否充足
- [ ] 检查日志是否有 ERROR
- [ ] 检查监控告警

### 6.2 每周检查

- [ ] 备份数据库
- [ ] 清理过期日志
- [ ] 检查磁盘使用情况
- [ ] 检查证书有效期
- [ ] 检查依赖版本更新

### 6.3 每月检查

- [ ] 安全补丁更新
- [ ] 性能优化
- [ ] 容量规划
- [ ] 灾备演练

## 7. 应急响应

### 7.1 服务故障

```bash
# 1. 查看服务状态
kubectl get pods -n education

# 2. 查看服务日志
kubectl logs <pod-name> -n education

# 3. 查看事件
kubectl describe pod <pod-name> -n education

# 4. 重启服务
kubectl rollout restart deployment/<service-name> -n education
```

### 7.2 数据库故障

```bash
# 1. 检查数据库连接
mysql -u root -p -e "SELECT 1"

# 2. 查看连接数
SHOW STATUS LIKE 'Threads_connected';

# 3. 杀掉异常连接
SHOW PROCESSLIST;
KILL <process_id>;
```

### 7.3 缓存故障

```bash
# 1. 检查 Redis 连接
redis-cli ping

# 2. 查看 Redis 状态
redis-cli info

# 3. 如果需要，清除缓存重启
redis-cli FLUSHALL
```
