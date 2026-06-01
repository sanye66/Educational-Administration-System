# 更新日志 (Changelog)

本项目遵循 [语义化版本](https://semver.org/lang/zh-CN/)。

## [1.0.0] - 2026-06-01

### ✨ 新增功能

#### 核心架构
- 基于 Spring Cloud Alibaba 的微服务架构
- API 网关（Spring Cloud Gateway）统一路由
- Nacos 服务注册发现与配置中心
- JWT + Spring Security 认证授权体系

#### 业务模块
- **用户管理** (`edu-user`)
  - 用户注册、登录、权限管理
  - 角色分配（管理员、教师、学生）
  - 个人信息维护
  
- **教师管理** (`edu-teacher`)
  - 教师信息管理
  - 职称、学历、研究方向
  - 教学工作量统计
  
- **学生管理** (`edu-student`)
  - 学生学籍管理
  - 班级、专业、院系信息
  - 入学、毕业状态跟踪
  
- **课程管理** (`edu-course`)
  - 课程创建与维护
  - 课程分类、学分、学时
  - 教师授课安排
  
- **选课管理** (`edu-selection`)
  - 选课任务发布
  - 学生在线选课
  - 选课冲突检测
  - 容量控制
  
- **成绩管理** (`edu-grade`)
  - 平时成绩、期中、期末成绩录入
  - 总评成绩计算
  - GPA 自动计算
  - 成绩单生成
  
- **考试管理** (`edu-exam`)
  - 考试计划安排
  - 考场分配
  - 考试时间表
  - 监考教师安排
  
- **排课管理** (`edu-schedule`)
  - 智能排课算法
  - 教室资源调度
  - 课程表生成
  - 冲突检测
  
- **教室管理** (`edu-classroom`)
  - 教室信息管理
  - 容量、设备、类型
  - 教室使用状态
  
- **毕业管理** (`edu-graduation`)
  - 毕业资格审核
  - 学分统计
  - 毕业论文管理
  - 学位授予
  
- **评价管理** (`edu-evaluation`)
  - 教学质量评价
  - 学生评教
  - 同行评议
  - 评价统计分析

#### AI 集成
- **AI 网关** (`edu-ai-gateway`)
  - 统一 AI 模型调用接口
  - 支持多种 AI 提供商（OpenAI、Azure、本地模型）
  - 智能对话接口
  - 文本向量化
  - 配额管理与限流
  - 使用统计和监控

#### 前端应用
- Vue 3 + TypeScript 现代化前端
- Ant Design Vue UI 组件库
- Pinia 状态管理
- 响应式设计
- 多角色界面（管理员、教师、学生）

### 🔧 技术特性

#### 数据持久化
- MyBatis-Plus ORM 框架
- MySQL 8.0+ 数据库
- Druid 连接池监控
- 多数据源支持（按业务分库）

#### 缓存系统
- Redis 分布式缓存
- Redisson 客户端
- 缓存穿透、击穿、雪崩防护
- 分布式锁实现

#### 消息队列
- RocketMQ 异步消息处理
- 事务消息支持
- 消息可靠性保证
- 削峰填谷

#### 监控告警
- Prometheus 指标采集
- Grafana 可视化面板
- Spring Boot Admin 应用监控
- Micrometer 指标收集
- Zipkin 链路追踪
- 基于 Nacos 的自动服务发现

#### API 文档
- SpringDoc OpenAPI 3.0
- Swagger UI 交互式文档
- 自动生成 API 文档

#### 安全机制
- JWT Token 认证
- Spring Security 权限控制
- BCrypt 密码加密
- XSS 防护
- SQL 注入防护
- CORS 跨域配置

### 📦 部署方案

#### Docker 容器化
- Docker Compose 编排
- 多阶段构建优化镜像
- 环境变量配置
- 健康检查

#### Kubernetes
- K8s 部署配置文件
- Service、Deployment、ConfigMap
- 命名空间隔离
- 资源限制

### 🛠️ 开发工具

#### 公共模块
- `edu-common-core`: 核心工具类
- `edu-common-security`: 安全配置
- `edu-common-redis`: Redis 配置
- `edu-common-mybatis`: MyBatis 配置
- `edu-common-log`: 日志配置
- `edu-common-swagger`: Swagger 配置
- `edu-common-monitor`: 监控配置

#### API 接口定义
- `edu-api-user`: 用户 API
- `edu-api-teacher`: 教师 API
- `edu-api-student`: 学生 API
- `edu-api-course`: 课程 API
- `edu-api-classroom`: 教室 API

#### 自动化脚本
- Nacos 配置导入脚本
- Prometheus 自动同步脚本
- 数据库初始化脚本
- CI/CD 流水线配置

### 📚 文档

- README.md - 项目介绍和快速开始
- CONTRIBUTING.md - 贡献指南
- QUICKSTART.md - Prometheus 快速启动
- GRAFANA_SERVICE_DISCOVERY.md - Grafana 服务发现
- AI_GATEWAY.md - AI 网关文档
- Harness 规则文档
  - 命名规范
  - 接口调用规范
  - 异常处理规范
  - Bean 注入规范
  - 缓存问题

### 🎯 性能优化

- 数据库索引优化
- Redis 缓存策略
- 连接池调优
- 懒加载和分页查询
- 异步处理
- 批量操作

### 🔒 安全加固

- 敏感信息加密存储
- API 访问频率限制
- SQL 参数化查询
- 输入验证和过滤
- 会话管理
- 审计日志

### 📊 数据库设计

- 12 个独立数据库（按业务划分）
- 逻辑删除支持
- 审计字段（create_by, create_time, update_by, update_time）
- 外键关联优化
- 索引策略

### 🌐 国际化准备

- UTF-8 编码支持
- 中文注释和文档
- 时间格式标准化
- 字符集统一配置

### ⚙️ 配置管理

- Nacos 集中配置
- 环境隔离（dev/test/prod）
- 配置动态刷新
- 共享配置提取

### 🔄 CI/CD

- Maven 构建流程
- Harness CI/CD 集成
- 自动化测试
- Docker 镜像构建
- Kubernetes 部署

### 📝 代码质量

- Lombok 简化代码
- MapStruct 对象映射
- Hutool 工具库
- 统一异常处理
- 统一响应格式
- 参数校验

### 🐛 已知问题

- 部分模块需要完善单元测试
- 前端页面仍在开发中
- 部分 API 接口需要优化
- 监控告警规则需要补充

### 📋 待办事项

- [ ] 完善前端所有功能页面
- [ ] 增加单元测试覆盖率至 80%+
- [ ] 实现更多 AI 应用场景
- [ ] 添加移动端支持
- [ ] 优化排课算法
- [ ] 实现数据导出功能
- [ ] 添加消息通知系统
- [ ] 完善权限细粒度控制

---

## 版本说明

### 版本号格式

`MAJOR.MINOR.PATCH`

- **MAJOR**: 不兼容的 API 变更
- **MINOR**: 向后兼容的功能新增
- **PATCH**: 向后兼容的问题修正

### 更新类型

- ✨ **新增**: 新功能
- 🔧 **改进**: 现有功能优化
- 🐛 **修复**: Bug 修复
- 📚 **文档**: 文档更新
- 🔒 **安全**: 安全相关
- 🎯 **性能**: 性能优化
- 🔄 **重构**: 代码重构
- 📦 **依赖**: 依赖更新
- 🛠️ **工具**: 开发工具
- 🌐 **国际化**: 国际化支持

---

**注意**: 此更新日志从 v1.0.0 开始记录。之前的开发迭代未在此详细列出。

**最后更新**: 2026-06-01
