# 教学管理系统 - 项目结构与技术栈

## 项目概述

教学管理系统（Educational Administration System）是一个基于 Spring Cloud Alibaba 微服务架构的教学管理平台，提供学生、教师、课程、成绩、排课、考试、选课等核心功能。

## 技术栈

### 后端技术栈

| 分类 | 技术 | 版本 |
|------|------|------|
| **基础框架** | Spring Boot | 2.7.18 |
| **微服务框架** | Spring Cloud | 2021.0.8 |
| **微服务组件** | Spring Cloud Alibaba | 2021.0.5.0 |
| **Java 版本** | JDK | 11 |
| **构建工具** | Maven | 3.9+ |

#### 数据层

| 技术 | 说明 |
|------|------|
| MyBatis-Plus | ORM 持久层框架 |
| MySQL | 关系型数据库 |
| Druid | 数据库连接池 |
| Redis | 缓存数据库 |

#### 中间件

| 技术 | 说明 |
|------|------|
| Redisson | Redis 客户端 |
| RocketMQ | 消息队列 |

#### 安全与认证

| 技术 | 说明 |
|------|------|
| JWT | JSON Web Token 认证 |
| Spring Security | 安全框架 |

#### 监控与日志

| 技术 | 说明 |
|------|------|
| Spring Boot Admin | 监控面板 |
| Zipkin | 分布式追踪 |
| Logstash | 日志收集 |

#### API 文档

| 技术 | 说明 |
|------|------|
| SpringDoc OpenAPI | Swagger UI 文档 |

#### 工具类

| 技术 | 说明 |
|------|------|
| Hutool | 工具集 |
| MapStruct | 对象映射 |
| Lombok | 代码生成 |

### 前端技术栈

| 分类 | 技术 | 版本 |
|------|------|------|
| **框架** | React | 18.3.1 |
| **UI 组件库** | Ant Design | 5.15.0 |
| **路由** | React Router | 6.22.0 |
| **HTTP 客户端** | Axios | 1.6.7 |
| **状态管理** | Zustand | 4.5.0 |
| **图表** | ECharts | 5.5.0 |
| **构建工具** | Vite | 5.2.0 |
| **语言** | TypeScript | 5.4.2 |

---

## 项目架构

```
Educational-Administration-System/
├── edu-common/                 # 公共模块
│   ├── edu-common-core/        # 核心工具类
│   ├── edu-common-redis/      # Redis 封装
│   ├── edu-common-security/   # 安全模块
│   ├── edu-common-log/        # 日志模块
│   ├── edu-common-swagger/    # Swagger 配置
│   ├── edu-common-mybatis/    # MyBatis 配置
│   └── edu-common-monitor/    # 监控模块
│
├── edu-api/                    # API 接口模块（Feign Client）
│   ├── edu-api-user/          # 用户接口
│   ├── edu-api-student/       # 学生接口
│   ├── edu-api-teacher/       # 教师接口
│   ├── edu-api-course/        # 课程接口
│   └── edu-api-classroom/     # 教室接口
│
├── edu-gateway/               # 网关服务
├── edu-auth/                  # 认证服务
│
├── edu-services/              # 业务服务
│   ├── edu-user/             # 用户服务
│   ├── edu-student/          # 学生服务
│   ├── edu-teacher/          # 教师服务
│   ├── edu-course/           # 课程服务
│   ├── edu-classroom/        # 教室服务
│   ├── edu-selection/        # 选课服务
│   ├── edu-grade/            # 成绩服务
│   ├── edu-exam/             # 考试服务
│   ├── edu-schedule/         # 排课服务
│   ├── edu-graduation/       # 毕设服务
│   ├── edu-evaluation/       # 评价服务
│   └── edu-ai-gateway/       # AI 网关服务
│
├── edu-monitor/               # 监控服务
│
├── edu-frontend/             # 前端应用
│
├── config/                   # 配置文件
├── docker/                   # Docker 配置
├── k8s/                      # Kubernetes 配置
├── nacos-config/             # Nacos 配置
├── scripts/                  # 脚本工具
├── sql/                      # 数据库脚本
└── harness/                  # CI/CD 配置
```

---

## 微服务列表

| 服务名称 | 端口 | 说明 |
|----------|------|------|
| edu-gateway | 8080 | API 网关 |
| edu-auth | 8081 | 认证服务 |
| edu-user | 8082 | 用户服务 |
| edu-student | 8083 | 学生服务 |
| edu-teacher | 8084 | 教师服务 |
| edu-course | 8085 | 课程服务 |
| edu-classroom | 8086 | 教室服务 |
| edu-selection | 8087 | 选课服务 |
| edu-grade | 8088 | 成绩服务 |
| edu-exam | 8089 | 考试服务 |
| edu-schedule | 8090 | 排课服务 |
| edu-graduation | 8091 | 毕设服务 |
| edu-evaluation | 8092 | 评价服务 |
| edu-ai-gateway | 8093 | AI 网关服务 |
| edu-monitor | 8094 | 监控服务 |

---

## 数据库列表

| 数据库名称 | 说明 |
|------------|------|
| edu_user | 用户数据 |
| edu_teacher | 教师数据 |
| edu_student | 学生数据 |
| edu_course | 课程数据 |
| edu_classroom | 教室数据 |
| edu_selection | 选课数据 |
| edu_grade | 成绩数据 |
| edu_exam | 考试数据 |
| edu_schedule | 排课数据 |
| edu_graduation | 毕设数据 |
| edu_evaluation | 评价数据 |
| edu_ai_gateway | AI 网关数据 |

---

## 环境要求

### 后端

- JDK 11+
- Maven 3.9+
- MySQL 8.0+
- Redis 6.0+
- Nacos 2.x (服务注册与配置中心)

### 前端

- Node.js 18+
- npm 9+

### 基础设施

- Docker & Docker Compose
- Kubernetes (生产环境)
