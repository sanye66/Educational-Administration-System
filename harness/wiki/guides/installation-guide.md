# 安装配置指南

本文档提供教学管理系统的安装配置指南。

## 1. 环境要求

### 1.1 硬件要求

| 环境 | CPU | 内存 | 磁盘 |
|------|-----|------|------|
| 开发环境 | 4 核 | 8GB | 100GB |
| 测试环境 | 8 核 | 16GB | 200GB |
| 生产环境 | 16 核+ | 32GB+ | 500GB+ |

### 1.2 软件要求

| 软件 | 版本 | 说明 |
|------|------|------|
| JDK | 11+ | 必须 |
| Maven | 3.9+ | 构建工具 |
| MySQL | 8.0+ | 数据库 |
| Redis | 6.0+ | 缓存 |
| Node.js | 18+ | 前端构建 |
| Docker | 20.10+ | 容器 |
| Kubernetes | 1.24+ | 生产环境 |

## 2. 开发环境搭建

### 2.1 后端搭建

```bash
# 1. 克隆项目
git clone https://github.com/your-repo/educational-administration-system.git
cd educational-administration-system

# 2. 配置 Maven 镜像（可选）
# 修改 settings.xml 镜像地址

# 3. 编译项目
mvn clean install -DskipTests

# 4. 启动 Nacos（必须）
# 下载 Nacos: https://github.com/alibaba/nacos/releases
# 解压后运行: sh startup.sh -m standalone

# 5. 启动服务（按顺序）
cd edu-gateway
mvn spring-boot:run

# 新终端启动其他服务...
```

### 2.2 前端搭建

```bash
# 1. 进入前端目录
cd edu-frontend

# 2. 安装依赖
npm install

# 3. 配置 API 地址
# 修改 src/utils/request.ts 中的 baseURL

# 4. 启动开发服务器
npm run dev
```

### 2.3 Docker Compose 一键启动

```bash
# 启动所有服务
cd docker
docker-compose up -d

# 查看服务状态
docker-compose ps
```

## 3. 数据库配置

### 3.1 创建数据库

```sql
-- 创建数据库
CREATE DATABASE edu_user DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ... 其他数据库类似

-- 执行初始化脚本
source sql/schema/init.sql
```

### 3.2 Nacos 配置

1. 访问 Nacos 控制台：http://localhost:8848/nacos
2. 默认账号密码：nacos/nacos
3. 导入配置：nacos-config/ 目录下的配置文件

## 4. 服务配置说明

### 4.1 通用配置

```yaml
spring:
  application:
    name: edu-user
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
      config:
        server-addr: localhost:8848
        file-extension: yml
  datasource:
    url: jdbc:mysql://localhost:3306/edu_user
    username: root
    password: root
  redis:
    host: localhost
    port: 6379
```

### 4.2 各服务端口配置

| 服务 | 端口 | 配置文件 |
|------|------|----------|
| gateway | 8080 | edu-gateway/src/main/resources/application.yml |
| auth | 8081 | edu-auth/src/main/resources/application.yml |
| user | 8082 | edu-services/edu-user/src/main/resources/application.yml |
| student | 8083 | edu-services/edu-student/src/main/resources/application.yml |
| teacher | 8084 | edu-services/edu-teacher/src/main/resources/application.yml |
| course | 8085 | edu-services/edu-course/src/main/resources/application.yml |

## 5. Kubernetes 部署

### 5.1 部署前准备

```bash
# 1. 创建命名空间
kubectl create namespace education

# 2. 创建 Secret
kubectl create secret docker-registry edu-registry \
  --docker-server=your-registry.com \
  --docker-username=xxx \
  --docker-password=xxx \
  -n education
```

### 5.2 部署脚本

```bash
# 部署所有服务
./scripts/deploy-k8s.sh prod

# 部署单个服务
kubectl apply -f k8s/edu-user-deployment.yaml -n education
```

### 5.3 验证部署

```bash
# 查看 Pod 状态
kubectl get pods -n education

# 查看服务
kubectl get svc -n education

# 查看日志
kubectl logs -f deployment/edu-gateway -n education
```

## 6. 配置参数说明

### 6.1 业务参数

```yaml
edu:
  system:
    # 最大登录失败次数
    max-login-fail: 5
    # token 过期时间（小时）
    token-expire-hours: 2
  course:
    # 课程最大容量
    max-capacity: 100
    # 选课开始时间
    selection-start-time: "09:00"
```

### 6.2 数据库连接池

```yaml
spring:
  datasource:
    druid:
      # 初始连接数
      initial-size: 5
      # 最大连接数
      max-active: 20
      # 最小空闲连接
      min-idle: 5
      # 获取连接超时
      max-wait: 60000
```

### 6.3 Redis 配置

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    database: 0
    timeout: 3000ms
    lettuce:
      pool:
        max-active: 8
        max-wait: -1ms
        max-idle: 8
        min-idle: 0
```

## 7. 安全配置

### 7.1 JWT 配置

```yaml
jwt:
  # JWT 密钥（生产环境必须修改）
  secret: your-secret-key-change-in-production
  # 过期时间（毫秒）
  expiration: 7200000
```

### 7.2 CORS 配置

```yaml
spring:
  cloud:
    gateway:
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins: "http://localhost:3000"
            allowedMethods: "*"
            allowedHeaders: "*"
            allowCredentials: true
```

## 8. 常见问题

### 8.1 端口被占用

```bash
# 查看端口占用
netstat -tlnp | grep 8080

# 杀掉进程
kill -9 <pid>
```

### 8.2 数据库连接失败

1. 检查 MySQL 服务是否启动
2. 检查数据库名称是否正确
3. 检查用户名密码是否正确
4. 检查防火墙是否放行端口

### 8.3 Nacos 服务注册失败

1. 检查 Nacos 是否启动
2. 检查网络连通性
3. 检查服务配置的服务名是否正确
