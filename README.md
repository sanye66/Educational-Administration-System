# 教学管理系统 (Educational Administration System)

[![Java](https://img.shields.io/badge/Java-11-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2021.0.8-blue.svg)](https://spring.io/projects/spring-cloud)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📖 项目简介

教学管理系统是一个基于 **Spring Cloud Alibaba** 微服务架构的现代化教育管理平台，提供完整的教务管理解决方案。系统采用前后端分离设计，支持用户管理、课程管理、选课、成绩管理、考试安排、毕业管理等核心功能。

### ✨ 核心特性

- 🏗️ **微服务架构** - 基于 Spring Cloud Alibaba 的分布式系统设计
- 🔐 **统一认证授权** - JWT + Spring Security 实现安全认证
- 🎯 **服务治理** - Nacos 服务注册发现与配置中心
- 💾 **数据持久化** - MyBatis-Plus + MySQL + Druid 连接池
- 🚀 **高性能缓存** - Redis + Redisson 分布式缓存
- 📨 **消息队列** - RocketMQ 异步消息处理
- 📊 **监控告警** - Prometheus + Grafana + Spring Boot Admin
- 🤖 **AI 集成** - 统一 AI Gateway 支持智能对话和文本处理
- 🌐 **API 网关** - Spring Cloud Gateway 统一路由和限流
- 📝 **接口文档** - SpringDoc OpenAPI 3.0 自动生成

## 🏛️ 系统架构

```
┌─────────────────────────────────────────────────────────────┐
│                      前端应用 (Vue3 + TS)                     │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ HTTP/REST
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   API Gateway (edu-gateway)                  │
│              路由转发 | 负载均衡 | 限流熔断                    │
└─────────────────────────────────────────────────────────────┘
                              │
              ┌───────────────┼───────────────┐
              │               │               │
              ▼               ▼               ▼
    ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
    │  Auth Service│ │ User Service │ │AI Gateway    │
    │  (edu-auth)  │ │  (edu-user)  │ │(edu-ai-gw)   │
    └──────────────┘ └──────────────┘ └──────────────┘
              │               │               │
              ▼               ▼               ▼
    ┌────────────────────────────────────────────────┐
    │          业务微服务集群                          │
    │  ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐  │
    │  │Teacher │ │Student │ │ Course │ │Classroom│  │
    │  └────────┘ └────────┘ └────────┘ └────────┘  │
    │  ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐  │
    │  │Selection│ │ Grade  │ │  Exam  │ │Schedule │  │
    │  └────────┘ └────────┘ └────────┘ └────────┘  │
    │  ┌────────┐ ┌────────┐                         │
    │  │Graduatn│ │Evalua. │                         │
    │  └────────┘ └────────┘                         │
    └────────────────────────────────────────────────┘
              │
              ▼
    ┌────────────────────────────────────────────────┐
    │           基础设施层                             │
    │  Nacos | MySQL | Redis | RocketMQ | Prometheus │
    └────────────────────────────────────────────────┘
```

## 📦 技术栈

### 后端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 11 | 开发语言 |
| Spring Boot | 2.7.18 | 基础框架 |
| Spring Cloud | 2021.0.8 | 微服务框架 |
| Spring Cloud Alibaba | 2021.0.5.0 | 阿里微服务套件 |
| MyBatis-Plus | 3.5.6 | ORM 框架 |
| MySQL | 8.0+ | 关系型数据库 |
| Druid | 1.2.21 | 数据库连接池 |
| Redis | 6.0+ | 缓存数据库 |
| Redisson | 3.17.7 | Redis 客户端 |
| RocketMQ | 4.x | 消息队列 |
| Nacos | 2.x | 服务注册与配置中心 |
| Spring Cloud Gateway | - | API 网关 |
| Spring Security | - | 安全框架 |
| JWT (JJWT) | 0.12.5 | Token 认证 |
| SpringDoc | 1.7.0 | API 文档 |
| Lombok | 1.18.30 | 代码简化 |
| MapStruct | 1.5.5 | 对象映射 |
| Hutool | 5.8.26 | Java 工具库 |

### 监控与运维

| 技术 | 说明 |
|------|------|
| Prometheus | 指标采集 |
| Grafana | 可视化监控 |
| Spring Boot Admin | 应用监控 |
| Micrometer | 指标收集 |
| Zipkin | 链路追踪 |
| Logstash | 日志收集 |

### 前端技术

| 技术 | 说明 |
|------|------|
| Vue 3 | 前端框架 |
| TypeScript | 类型安全 |
| Vite | 构建工具 |
| Ant Design Vue | UI 组件库 |
| Pinia | 状态管理 |
| Axios | HTTP 客户端 |

## 🗂️ 项目结构

```
Educational-Administration-System/
├── edu-common/                  # 公共模块
│   ├── edu-common-core/        # 核心工具类
│   ├── edu-common-security/    # 安全配置
│   ├── edu-common-redis/       # Redis 配置
│   ├── edu-common-mybatis/     # MyBatis 配置
│   ├── edu-common-log/         # 日志配置
│   ├── edu-common-swagger/     # Swagger 配置
│   └── edu-common-monitor/     # 监控配置
├── edu-api/                     # API 接口定义
│   ├── edu-api-user/           # 用户 API
│   ├── edu-api-teacher/        # 教师 API
│   ├── edu-api-student/        # 学生 API
│   ├── edu-api-course/         # 课程 API
│   └── edu-api-classroom/      # 教室 API
├── edu-gateway/                 # API 网关
├── edu-auth/                    # 认证服务
├── edu-services/                # 业务微服务
│   ├── edu-user/               # 用户服务
│   ├── edu-teacher/            # 教师服务
│   ├── edu-student/            # 学生服务
│   ├── edu-course/             # 课程服务
│   ├── edu-classroom/          # 教室服务
│   ├── edu-selection/          # 选课服务
│   ├── edu-grade/              # 成绩服务
│   ├── edu-exam/               # 考试服务
│   ├── edu-schedule/           # 排课服务
│   ├── edu-graduation/         # 毕业服务
│   ├── edu-evaluation/         # 评价服务
│   └── edu-ai-gateway/         # AI 网关服务
├── edu-monitor/                 # 监控服务
├── edu-frontend/                # 前端应用
├── nacos-config/                # Nacos 配置文件
├── docker/                      # Docker 配置
├── k8s/                         # Kubernetes 配置
├── sql/                         # 数据库脚本
├── scripts/                     # 自动化脚本
├── harness/                     # CI/CD 配置
└── config/                      # 通用配置
```

## 🚀 快速开始

### 前置要求

- JDK 11+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- Nacos 2.x
- Node.js 16+ (前端开发)
- Docker & Docker Compose (可选，容器化部署)

### 环境准备

#### 1. 克隆项目

```bash
git clone https://github.com/sanye66/Educational-Administration-System.git
cd Educational-Administration-System
```

#### 2. 初始化数据库

```bash
mysql -u root -p < sql/schema/init.sql
```

#### 3. 启动中间件（使用 Docker）

```bash
cd docker
docker-compose up -d nacos mysql redis rocketmq prometheus grafana
```

#### 4. 导入 Nacos 配置

```bash
cd nacos-config
python import-nacos-config.ps1
```

#### 5. 编译项目

```bash
mvn clean install -DskipTests
```

#### 6. 启动微服务

**方式一：逐个启动**

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

# 启动其他服务...
```

**方式二：使用 Docker Compose**

```bash
cd docker
docker-compose up -d
```

#### 7. 启动前端

```bash
cd edu-frontend
npm install
npm run dev
```

### 访问地址

| 服务 | 地址 | 说明 |
|------|------|------|
| 前端应用 | http://localhost:5173 | Vue3 前端 |
| API 网关 | http://localhost:8080 | 统一入口 |
| Nacos 控制台 | http://localhost:8848/nacos | 服务管理 (nacos/nacos) |
| Spring Boot Admin | http://localhost:8093 | 应用监控 |
| Prometheus | http://localhost:9090 | 指标监控 |
| Grafana | http://localhost:3300 | 可视化面板 (admin/admin) |
| Swagger UI | http://localhost:8080/doc.html | API 文档 |

## 📚 核心功能模块

### 1. 用户服务 (edu-user) - 端口 8082
**核心功能：**
- 用户注册、登录认证、权限管理
- 角色分配（管理员 ADMIN、教师 TEACHER、学生 STUDENT）
- 个人信息维护（头像、联系方式、性别等）
- 用户状态管理（正常、禁用、锁定）
- 密码修改与重置

**技术特点：**
- BCrypt 密码加密存储
- JWT Token 生成与验证
- 逻辑删除支持
- Redis 缓存用户信息

### 2. 教师服务 (edu-teacher) - 端口 8083
**核心功能：**
- 教师基本信息管理（工号、姓名、联系方式）
- 职称管理（教授、副教授、讲师等）
- 学历学位信息管理
- 研究方向记录
- 入职日期跟踪
- 所属院系关联
- 教学工作量统计

**数据关联：**
- 关联 sys_user 表（user_id）
- 唯一索引：教师工号（teacher_no）

### 3. 学生服务 (edu-student) - 端口 8084
**核心功能：**
- 学生学籍信息管理（学号、姓名、基本信息）
- 班级、专业、院系层级关联
- 入学年份与学制管理
- 学籍状态跟踪（在读、休学、毕业、退学）
- 毕业年限计算
- 学生档案维护

**数据关联：**
- 关联 sys_user 表（user_id）
- 唯一索引：学号（student_no）
- 外键关联：department_id, major_id, class_id

### 4. 课程服务 (edu-course) - 端口 8085
**核心功能：**
- 课程创建与维护（课程编号、名称、描述）
- 课程分类管理（必修课、选修课、通识课等）
- 学分与学时设置
- 授课教师关联
- 课程容量控制（最大容量、当前选课人数）
- 学年学期管理
- 课程状态流转（草稿、待审核、已发布、已结束）
- 课程查询与筛选

**业务规则：**
- 课程编号唯一性约束
- 选课人数不能超过最大容量
- 支持按学年、学期、分类多维度查询

### 5. 选课服务 (edu-selection) - 端口 8087
**核心功能：**
- 选课任务发布与管理
- 学生在线选课操作
- 选课冲突检测（时间冲突、先修课程检查）
- 课程容量实时控制
- 选课结果查询与统计
- 退课功能支持
- 选课时间段控制
- 高并发选课处理

**技术特点：**
- Redis 分布式锁防止超选
- RocketMQ 异步处理选课请求
- 数据库唯一索引防止重复选课
- 事务保证数据一致性

**业务流程：**
1. 管理员发布选课任务（设置时间窗口）
2. 学生在选课时间内提交选课申请
3. 系统检查容量、冲突等条件
4. 选课成功/失败结果反馈
5. 生成选课记录

### 6. 成绩服务 (edu-grade) - 端口 8088
**核心功能：**
- 平时成绩录入与管理
- 期中考试成绩记录
- 期末考试成绩录入
- 总评成绩自动计算（可配置权重）
- GPA（绩点）自动计算
- 成绩状态管理（草稿、已提交、已发布）
- 成绩单生成与导出
- 成绩统计分析（平均分、最高分、最低分、分布情况）
- 补考成绩管理

**计算规则：**
- 总评成绩 = 平时成绩 × 权重1 + 期中成绩 × 权重2 + 期末成绩 × 权重3
- GPA 根据分数段自动换算
- 支持自定义成绩计算公式

**权限控制：**
- 教师：录入和修改自己所授课程成绩
- 学生：查看自己的成绩
- 管理员：查看所有成绩、统计分析

### 7. 考试服务 (edu-exam) - 端口 8089
**核心功能：**
- 考试计划制定与管理
- 考场智能分配（基于课程人数、教室容量）
- 考试时间安排（避免时间冲突）
- 监考教师指派
- 考生座位编排
- 考试类型管理（期中、期末、补考、重修）
- 考试时间表生成与发布
- 考试冲突检测

**智能算法：**
- 考场分配算法（最优匹配教室容量）
- 时间冲突检测（同一学生不能同时参加多场考试）
- 监考教师负载均衡

**数据关联：**
- 关联课程表（course_id）
- 关联教室表（classroom_id）
- 关联教师表（监考官）

### 8. 排课服务 (edu-schedule) - 端口 8090
**核心功能：**
- 智能排课算法实现
- 教室资源调度与优化
- 课程时间表生成
- 排课冲突检测（教师时间、教室占用、班级课程）
- 手动调整排课结果
- 课程表查询与导出
- 学期课表管理

**排课约束：**
- 教师时间可用性
- 教室容量与类型匹配
- 班级课程时间不冲突
- 同一教室同一时间只能有一门课
- 考虑课程优先级和偏好时间

**技术特点：**
- 基于约束满足问题（CSP）算法
- 支持手动干预和自动排课结合
- 排课结果可视化展示

### 9. 教室服务 (edu-classroom) - 端口 8086
**核心功能：**
- 教室基本信息管理（教室编号、名称、位置）
- 教室容量设置
- 教室类型分类（普通教室、多媒体教室、实验室、机房等）
- 教学设备登记（投影仪、电脑、音响等）
- 教室使用状态管理（可用、维修中、停用）
- 教室借用申请与审批
- 教室使用情况统计

**查询功能：**
- 按容量筛选教室
- 按类型筛选教室
- 按时间段查询空闲教室
- 按设备需求查询教室

### 10. 毕业服务 (edu-graduation) - 端口 8091
**核心功能：**
- 毕业资格审核（学分要求、课程完成情况）
- 毕业学分统计与核算
- 毕业论文管理（选题、开题、答辩）
- 学位授予管理
- 毕业证书信息生成
- 毕业名单管理
- 延期毕业处理
- 校友信息管理

**审核规则：**
- 必修课程全部通过
- 总学分达到毕业要求
- GPA 达到学位授予标准
- 无违纪处分记录
- 毕业论文通过答辩

**业务流程：**
1. 系统自动筛查符合毕业条件的学生
2. 人工复核特殊情况
3. 生成毕业名单
4. 学位评定委员会审核
5. 颁发毕业证书和学位证书

### 11. 评价服务 (edu-evaluation) - 端口 8092
**核心功能：**
- 教学质量评价体系
- 学生评教（对任课教师打分和留言）
- 同行评议（教师互评）
- 督导评价（教学督导组评价）
- 评价指标管理（教学内容、教学方法、教学效果等维度）
- 评价结果统计分析
- 评价报告生成
- 历史评价数据对比

**评价维度：**
- 教学内容（40%）：内容充实、重点突出
- 教学方法（30%）：方法灵活、互动良好
- 教学效果（20%）：目标达成、学生收获
- 教学态度（10%）：认真负责、准时上下课

**数据分析：**
- 教师评分排名
- 各维度得分分析
- 历年趋势对比
- 文字评价情感分析

### 12. AI 网关服务 (edu-ai-gateway) - 端口 8095
**核心功能：**
- 统一 AI 模型调用接口（屏蔽不同提供商差异）
- 智能对话接口（支持 GPT-4、GPT-3.5、LLaMA 等模型）
- 文本向量化接口（Embedding）
- 多模型提供商支持（OpenAI、Azure OpenAI、本地模型）
- 智能路由与负载均衡（根据请求类型选择最优模型）
- 用户配额管理（Token 使用限制）
- API 限流防滥用
- 使用记录与统计
- 响应缓存优化

**API 接口：**
- `POST /api/ai/chat` - AI 对话
- `POST /api/ai/chat/stream` - 流式对话
- `POST /api/ai/embeddings` - 文本向量化
- `GET /api/ai/models` - 获取可用模型列表
- `GET /api/ai/quota/{userId}/{modelId}` - 查询配额

**应用场景：**
- 智能客服助手
- 课程内容自动生成
- 作业批改辅助
- 学习建议推荐
- 文本相似度计算

**技术特点：**
- Redis 缓存常用响应
- 分布式限流（基于 IP 和用户）
- 自动重试和故障转移
- 详细的 Token 使用统计

## 🔧 配置说明

### Nacos 配置中心

所有微服务的配置都集中在 `nacos-config` 目录：

```
nacos-config/
├── shared-config.yml          # 共享配置
├── edu-gateway.yml            # 网关配置
├── edu-auth.yml               # 认证服务配置
├── edu-user.yml               # 用户服务配置
├── edu-teacher.yml            # 教师服务配置
├── edu-student.yml            # 学生服务配置
├── edu-course.yml             # 课程服务配置
├── edu-classroom.yml          # 教室服务配置
├── edu-selection.yml          # 选课服务配置
├── edu-grade.yml              # 成绩服务配置
├── edu-exam.yml               # 考试服务配置
├── edu-schedule.yml           # 排课服务配置
├── edu-graduation.yml         # 毕业服务配置
├── edu-evaluation.yml         # 评价服务配置
├── edu-ai-gateway.yml         # AI 网关配置
└── seataServer.properties     # Seata 配置
```

### 主要配置项

```yaml
server:
  port: 8085  # 服务端口

spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848  # Nacos 地址
      config:
        server-addr: localhost:8848
  
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/edu_user?useUnicode=true&characterEncoding=utf8
    username: root
    password: your_password

  redis:
    host: localhost
    port: 6379
```

## 📊 监控与运维

### Prometheus 自动服务发现

系统实现了基于 Nacos 的 Prometheus 自动服务发现，新服务启动后会自动加入监控。

详细文档：[QUICKSTART.md](QUICKSTART.md)

### Grafana 仪表板

预配置的监控面板包括：
- JVM 内存和 GC 监控
- HTTP 请求 QPS 和响应时间
- 数据库连接池状态
- Redis 缓存命中率
- 微服务健康状态

详细文档：[GRAFANA_SERVICE_DISCOVERY.md](GRAFANA_SERVICE_DISCOVERY.md)

### Spring Boot Admin

访问 http://localhost:8093 查看所有微服务的：
- 实时健康状态
- 环境变量和配置
- 线程 dump
- Heap dump
- 日志级别动态调整

## 🧪 测试

### 单元测试

```bash
mvn test
```

### 接口测试

启动服务后访问 Swagger UI：http://localhost:8080/doc.html

## 📦 部署

### Docker 部署

```bash
cd docker
docker-compose up -d
```

### Kubernetes 部署

```bash
kubectl apply -f k8s/
```

### 生产环境建议

1. **数据库**：使用主从复制，读写分离
2. **Redis**：使用哨兵模式或集群模式
3. **Nacos**：集群部署，保证高可用
4. **RocketMQ**：集群部署，启用持久化
5. **监控**：配置告警规则，设置通知渠道
6. **日志**：集中式日志管理（ELK）
7. **备份**：定期备份数据库和配置文件

## 🔒 安全说明

- 默认管理员账号：`admin` / `123456`（首次登录后请立即修改密码）
- JWT Token 有效期：2 小时
- 生产环境请修改所有默认密码
- 启用 HTTPS
- 配置防火墙规则
- 定期更新依赖包

## 🤝 贡献指南

欢迎贡献代码！请遵循以下步骤：

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

### 代码规范

请参考 `harness/rules/` 目录中的规范文档：
- [命名规范](harness/rules/命名规范.md)
- [接口调用规范](harness/rules/接口调用规范.md)
- [异常处理规范](harness/rules/异常处理规范.md)
- [Bean 注入规范](harness/rules/Bean注入规范.md)
- [缓存问题](harness/rules/缓存问题.md)

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 📞 联系方式

- 项目主页：https://github.com/sanye66/Educational-Administration-System
- 问题反馈：https://github.com/sanye66/Educational-Administration-System/issues

## 🙏 致谢

感谢以下开源项目：
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Cloud Alibaba](https://spring.io/projects/spring-cloud-alibaba)
- [MyBatis-Plus](https://baomidou.com/)
- [Nacos](https://nacos.io/)
- [Vue.js](https://vuejs.org/)

---

**最后更新**: 2026-06-01  
**版本**: 1.0.0
