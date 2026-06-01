# 部署指南 (Deployment Guide)

## 📋 目录

- [环境要求](#环境要求)
- [本地开发环境](#本地开发环境)
- [Docker 部署](#docker-部署)
- [Kubernetes 部署](#kubernetes-部署)
- [生产环境部署](#生产环境部署)
- [配置说明](#配置说明)
- [监控与日志](#监控与日志)
- [故障排查](#故障排查)

## 环境要求

### 基础环境

| 软件 | 版本 | 说明 |
|------|------|------|
| JDK | 11+ | Java 运行环境 |
| Maven | 3.6+ | 构建工具 |
| MySQL | 8.0+ | 关系型数据库 |
| Redis | 6.0+ | 缓存数据库 |
| Nacos | 2.x | 服务注册与配置中心 |
| RocketMQ | 4.x | 消息队列（可选） |
| Node.js | 16+ | 前端开发（可选） |

### 硬件要求（生产环境）

| 组件 | 最低配置 | 推荐配置 |
|------|---------|---------|
| CPU | 4 核 | 8+ 核 |
| 内存 | 8 GB | 16+ GB |
| 磁盘 | 100 GB | 500+ GB SSD |
| 网络 | 100 Mbps | 1 Gbps |

## 本地开发环境

### 1. 克隆项目

```bash
git clone https://github.com/sanye66/Educational-Administration-System.git
cd Educational-Administration-System
```

### 2. 初始化数据库

```bash
mysql -u root -p < sql/schema/init.sql
```

### 3. 启动中间件

**使用 Docker（推荐）**

```bash
cd docker
docker-compose up -d nacos mysql redis
```

**或手动安装**

- 下载并启动 Nacos：https://nacos.io/zh-cn/docs/quick-start.html
- 安装 MySQL 并执行初始化脚本
- 安装并启动 Redis

### 4. 导入 Nacos 配置

```bash
cd nacos-config
# Windows PowerShell
.\import-nacos-config.ps1

# 或在 Nacos 控制台手动导入配置文件
```

### 5. 编译项目

```bash
mvn clean install -DskipTests
```

### 6. 启动服务

**方式一：IDEA 启动**

1. 打开 IDEA，导入 Maven 项目
2. 配置各服务的 VM Options：
   ```
   -Dspring.cloud.nacos.config.server-addr=localhost:8848
   -Dspring.cloud.nacos.discovery.server-addr=localhost:8848
   ```
3. 按顺序启动服务：
   - edu-gateway（端口 8080）
   - edu-auth（端口 8081）
   - edu-user（端口 8082）
   - 其他业务服务

**方式二：命令行启动**

```bash
# 启动网关
cd edu-gateway
mvn spring-boot:run

# 启动认证服务
cd edu-auth
mvn spring-boot:run

# 启动用户服务
cd edu-services/edu-user
mvn spring-boot:run

# ... 依次启动其他服务
```

### 7. 启动前端（可选）

```bash
cd edu-frontend
npm install
npm run dev
```

### 8. 验证部署

访问以下地址确认服务正常：

- API 网关：http://localhost:8080
- Nacos 控制台：http://localhost:8848/nacos（nacos/nacos）
- Swagger 文档：http://localhost:8080/doc.html

## Docker 部署

### 1. 前置条件

- 安装 Docker：https://docs.docker.com/get-docker/
- 安装 Docker Compose：https://docs.docker.com/compose/install/

### 2. 构建镜像

```bash
# 在项目根目录
mvn clean package -DskipTests

# 构建所有服务镜像
cd docker
docker-compose build
```

### 3. 启动服务

```bash
# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

### 4. 常用命令

```bash
# 停止所有服务
docker-compose down

# 重启特定服务
docker-compose restart edu-gateway

# 查看资源使用
docker stats

# 进入容器
docker exec -it edu-gateway bash
```

### 5. Docker Compose 配置示例

```yaml
version: '3.8'

services:
  # Nacos
  nacos:
    image: nacos/nacos-server:v2.2.0
    container_name: edu-nacos
    environment:
      MODE: standalone
    ports:
      - "8848:8848"
      - "9848:9848"
    volumes:
      - ./nacos-data:/home/nacos/data

  # MySQL
  mysql:
    image: mysql:8.0
    container_name: edu-mysql
    environment:
      MYSQL_ROOT_PASSWORD: your_password
      MYSQL_DATABASE: edu_user
    ports:
      - "3306:3306"
    volumes:
      - ./mysql-data:/var/lib/mysql
      - ../sql/schema/init.sql:/docker-entrypoint-initdb.d/init.sql

  # Redis
  redis:
    image: redis:6.2
    container_name: edu-redis
    ports:
      - "6379:6379"
    command: redis-server --requirepass your_redis_password

  # Gateway
  edu-gateway:
    build:
      context: ..
      dockerfile: edu-gateway/Dockerfile
    container_name: edu-gateway
    ports:
      - "8080:8080"
    environment:
      NACOS_SERVER_ADDR: nacos:8848
    depends_on:
      - nacos

  # Auth Service
  edu-auth:
    build:
      context: ..
      dockerfile: edu-auth/Dockerfile
    container_name: edu-auth
    ports:
      - "8081:8081"
    environment:
      NACOS_SERVER_ADDR: nacos:8848
    depends_on:
      - nacos
      - mysql
      - redis

  # ... 其他服务类似配置
```

## Kubernetes 部署

### 1. 前置条件

- Kubernetes 集群（v1.20+）
- kubectl 配置完成
- Helm 3.x（可选）

### 2. 创建命名空间

```bash
kubectl apply -f k8s/namespace.yml
```

### 3. 配置 ConfigMap

```bash
kubectl create configmap edu-config \
  --from-file=nacos-config/ \
  -n educational-system
```

### 4. 部署中间件

```bash
# 使用 Helm 安装 Nacos
helm repo add nacos https://nacos.io/charts
helm install nacos nacos/nacos -n educational-system

# 安装 MySQL
kubectl apply -f k8s/mysql-statefulset.yml

# 安装 Redis
kubectl apply -f k8s/redis-deployment.yml
```

### 5. 部署微服务

```bash
# 部署网关
kubectl apply -f k8s/gateway-deployment.yml

# 部署认证服务
kubectl apply -f k8s/auth-deployment.yml

# 部署业务服务
kubectl apply -f k8s/services/
```

### 6. 配置 Ingress

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: edu-ingress
  namespace: educational-system
spec:
  rules:
  - host: edu.yourdomain.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: edu-gateway
            port:
              number: 8080
```

### 7. 验证部署

```bash
# 查看所有 Pod
kubectl get pods -n educational-system

# 查看服务
kubectl get svc -n educational-system

# 查看日志
kubectl logs -f deployment/edu-gateway -n educational-system
```

## 生产环境部署

### 1. 架构建议

```
                    ┌─────────────┐
                    │   Nginx LB  │
                    └──────┬──────┘
                           │
              ┌────────────┼────────────┐
              │            │            │
        ┌─────▼─────┐ ┌───▼────┐ ┌────▼─────┐
        │ Gateway 1 │ │ GW 2   │ │  GW 3    │
        └─────┬─────┘ └───┬────┘ └────┬─────┘
              │            │            │
              └────────────┼────────────┘
                           │
         ┌─────────────────┼─────────────────┐
         │                 │                 │
    ┌────▼────┐      ┌────▼────┐      ┌────▼────┐
    │Service 1│      │Service 2│      │Service 3│
    └─────────┘      └─────────┘      └─────────┘
```

### 2. 高可用配置

#### Nacos 集群

```yaml
# application.properties
nacos.core.auth.enabled=true
nacos.core.auth.system.type=nacos
cluster.name=edu-cluster
```

#### MySQL 主从复制

```sql
-- 主库配置
[mysqld]
server-id=1
log-bin=mysql-bin
binlog-format=ROW

-- 从库配置
[mysqld]
server-id=2
relay-log=mysql-relay-bin
```

#### Redis 哨兵模式

```conf
# sentinel.conf
sentinel monitor mymaster 192.168.1.100 6379 2
sentinel auth-pass mymaster your_password
```

#### RocketMQ 集群

```bash
# broker-a.properties
brokerClusterName=DefaultCluster
brokerName=broker-a
brokerId=0
namesrvAddr=192.168.1.100:9876;192.168.1.101:9876
```

### 3. 性能调优

#### JVM 参数

```bash
-Xms4g -Xmx4g
-XX:MetaspaceSize=256m
-XX:MaxMetaspaceSize=512m
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/logs/heapdump.hprof
```

#### Tomcat 配置

```yaml
server:
  tomcat:
    threads:
      max: 200
      min-spare: 20
    accept-count: 100
    max-connections: 8192
```

#### 数据库连接池

```yaml
spring:
  datasource:
    druid:
      initial-size: 10
      min-idle: 10
      max-active: 50
      max-wait: 60000
```

### 4. 安全加固

```bash
# 防火墙规则
iptables -A INPUT -p tcp --dport 8080 -j ACCEPT
iptables -A INPUT -p tcp --dport 8848 -j ACCEPT
iptables -A INPUT -j DROP

# SSL 证书配置（Nginx）
ssl_certificate /etc/ssl/certs/edu.crt;
ssl_certificate_key /etc/ssl/private/edu.key;
ssl_protocols TLSv1.2 TLSv1.3;
```

## 配置说明

### 环境变量

```bash
# Nacos
NACOS_SERVER_ADDR=localhost:8848
NACOS_USERNAME=nacos
NACOS_PASSWORD=nacos

# MySQL
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_DATABASE=edu_user
MYSQL_USERNAME=root
MYSQL_PASSWORD=your_password

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=your_redis_password

# JWT
JWT_SECRET=your_jwt_secret_key_at_least_256_bits
JWT_EXPIRATION=7200000

# AI Gateway
OPENAI_API_KEY=sk-your-openai-key
AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

### Nacos 配置项

参考 `nacos-config/` 目录下的配置文件：

- `shared-config.yml`: 共享配置
- `edu-gateway.yml`: 网关配置
- `edu-auth.yml`: 认证配置
- 各业务服务配置

## 监控与日志

### Prometheus + Grafana

```bash
# 启动监控栈
cd docker
docker-compose up -d prometheus grafana

# 访问
# Prometheus: http://localhost:9090
# Grafana: http://localhost:3300 (admin/admin)
```

### Spring Boot Admin

```bash
# 启动监控服务
cd edu-monitor
mvn spring-boot:run

# 访问: http://localhost:8093
```

### 日志管理

#### 日志配置

```xml
<!-- logback-spring.xml -->
<appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>/logs/edu-service.log</file>
    <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
        <fileNamePattern>/logs/edu-service.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
        <maxFileSize>100MB</maxFileSize>
        <maxHistory>30</maxHistory>
    </rollingPolicy>
</appender>
```

#### ELK 集成（可选）

```yaml
# docker-compose.yml
elasticsearch:
  image: elasticsearch:7.17.0
  environment:
    - discovery.type=single-node

logstash:
  image: logstash:7.17.0
  volumes:
    - ./logstash.conf:/usr/share/logstash/pipeline/logstash.conf

kibana:
  image: kibana:7.17.0
  ports:
    - "5601:5601"
```

## 故障排查

### 常见问题

#### 1. 服务无法注册到 Nacos

**症状**: 服务启动后在 Nacos 控制台看不到

**解决**:
```bash
# 检查 Nacos 是否正常运行
curl http://localhost:8848/nacos/v1/console/health

# 检查网络连接
ping nacos-server

# 查看服务日志
tail -f logs/edu-service.log

# 确认配置正确
grep nacos application.yml
```

#### 2. 数据库连接失败

**症状**: 启动时报错 "Cannot connect to database"

**解决**:
```bash
# 测试数据库连接
mysql -h localhost -u root -p

# 检查数据库是否创建
show databases;

# 确认配置文件中的数据库信息
grep datasource application.yml

# 检查防火墙
telnet localhost 3306
```

#### 3. Redis 连接超时

**症状**: 缓存操作超时

**解决**:
```bash
# 测试 Redis 连接
redis-cli -h localhost -p 6379 ping

# 检查 Redis 密码
grep redis application.yml

# 查看 Redis 日志
docker logs edu-redis
```

#### 4. 网关路由失败

**症状**: 访问接口返回 404

**解决**:
```bash
# 检查网关路由配置
curl http://localhost:8080/actuator/gateway/routes

# 确认下游服务已启动
curl http://localhost:8082/actuator/health

# 查看网关日志
docker logs edu-gateway
```

#### 5. JWT Token 验证失败

**症状**: 接口返回 401 Unauthorized

**解决**:
```java
// 确认 JWT 密钥一致
@Value("${jwt.secret}")
private String secret;

// 检查 Token 是否过期
Claims claims = Jwts.parser()
    .setSigningKey(secret)
    .parseClaimsJws(token)
    .getBody();
```

### 性能问题排查

#### CPU 使用率高

```bash
# 查看线程使用情况
jstack <pid> > thread_dump.txt

# 分析热点方法
top -H -p <pid>
```

#### 内存泄漏

```bash
# 生成 Heap Dump
jmap -dump:format=b,file=heap.hprof <pid>

# 使用 MAT 分析
# https://www.eclipse.org/mat/
```

#### 慢查询

```sql
-- 开启慢查询日志
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 2;

-- 查看慢查询
SHOW VARIABLES LIKE 'slow_query%';
```

### 日志分析

```bash
# 查找错误日志
grep "ERROR" logs/*.log

# 统计异常频率
grep "Exception" logs/*.log | wc -l

# 实时监控日志
tail -f logs/*.log | grep -i error
```

## 备份与恢复

### 数据库备份

```bash
# 全量备份
mysqldump -u root -p --all-databases > backup_$(date +%Y%m%d).sql

# 单库备份
mysqldump -u root -p edu_user > edu_user_backup.sql

# 定时备份（crontab）
0 2 * * * /usr/bin/mysqldump -u root -pPASSWORD --all-databases > /backup/db_$(date +\%Y\%m\%d).sql
```

### 配置备份

```bash
# 备份 Nacos 配置
cp -r nacos-config/ nacos-config-backup-$(date +%Y%m%d)/

# 备份 Docker 配置
cp docker/docker-compose.yml docker-compose.yml.backup
```

### 恢复流程

```bash
# 恢复数据库
mysql -u root -p < backup_20260601.sql

# 恢复配置
cp -r nacos-config-backup-20260601/* nacos-config/

# 重启服务
docker-compose restart
```

## 升级指南

### 版本升级

```bash
# 1. 备份当前版本
git tag v1.0.0-backup
mysqldump -u root -p --all-databases > pre_upgrade_backup.sql

# 2. 拉取新版本
git pull origin main

# 3. 更新依赖
mvn clean install

# 4. 更新数据库 schema
mysql -u root -p < sql/schema/upgrade_v1.0_to_v1.1.sql

# 5. 更新 Nacos 配置
cd nacos-config
./import-nacos-config.ps1

# 6. 重启服务
docker-compose down
docker-compose up -d

# 7. 验证功能
curl http://localhost:8080/actuator/health
```

### 回滚策略

```bash
# 如果升级失败，快速回滚
git checkout v1.0.0
docker-compose down
docker-compose up -d
mysql -u root -p < pre_upgrade_backup.sql
```

---

**最后更新**: 2026-06-01  
**版本**: 1.0.0
