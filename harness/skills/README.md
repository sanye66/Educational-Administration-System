# Skills 技能索引

本文档列出所有可用的技能模板和脚本。

## 技能列表

| 技能 | 说明 | 文件 |
|------|------|------|
| Maven 构建 | Maven 项目的 CI/CD 流水线模板 | [maven-build.md](maven-build.md) |
| Spring Boot 开发 | Controller/Service/Mapper/Entity 模板 | [spring-boot-template.md](spring-boot-template.md) |
| Feign 客户端 | 微服务间调用的客户端模板 | [feign-client-template.md](feign-client-template.md) |
| Redis 缓存 | 缓存工具类和防击穿模板 | [redis-cache-template.md](redis-cache-template.md) |
| 异常处理 | 统一响应和全局异常处理模板 | [exception-handler-template.md](exception-handler-template.md) |
| 部署脚本 | Docker/K8s 部署和回滚脚本 | [deployment-script-template.md](deployment-script-template.md) |

## 使用说明

### 1. Maven 构建

参考 [maven-build.md](maven-build.md) 创建项目的 CI/CD 流水线。

### 2. 开发模板

复制 [spring-boot-template.md](spring-boot-template.md) 中的代码模板快速创建：
- Controller
- Service 接口和实现
- Mapper
- Entity
- DTO/VO

### 3. 服务调用

使用 [feign-client-template.md](feign-client-template.md) 创建 Feign 客户端：
- 定义接口
- 实现降级处理
- 配置超时和拦截器

### 4. 缓存操作

参考 [redis-cache-template.md](redis-cache-template.md)：
- Redis 配置
- 缓存工具类
- 防缓存击穿
- 缓存一致性处理

### 5. 异常处理

使用 [exception-handler-template.md](exception-handler-template.md)：
- 统一响应格式
- 错误码定义
- 全局异常处理
- 日志规范

### 6. 部署运维

参考 [deployment-script-template.md](deployment-script-template.md)：
- Docker 构建
- K8s 部署
- 回滚脚本
- 健康检查

## 规范引用

本技能包基于以下规范生成：
- [命名规范](../rules/命名规范.md)
- [Bean注入规范](../rules/Bean注入规范.md)
- [接口调用规范](../rules/接口调用规范.md)
- [缓存问题规范](../rules/缓存问题.md)
- [异常处理规范](../rules/异常处理规范.md)
- [回滚策略规范](../rules/回滚策略.md)
- [公司标准规范](../rules/公司标准.md)
