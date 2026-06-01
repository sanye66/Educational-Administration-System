# 安全策略 (Security Policy)

## 📋 目录

- [支持版本](#支持版本)
- [报告安全漏洞](#报告安全漏洞)
- [安全最佳实践](#安全最佳实践)
- [已知安全问题](#已知安全问题)
- [安全更新流程](#安全更新流程)

## 支持版本

以下版本目前支持安全更新：

| 版本 | 支持状态 | 说明 |
|------|---------|------|
| 1.0.x | ✅ 活跃支持 | 当前稳定版本 |
| < 1.0 | ❌ 不支持 | 初始开发版本 |

## 报告安全漏洞

我们非常重视安全问题。如果您发现安全漏洞，请**不要**在 GitHub Issues 中公开披露。

### 报告方式

请通过以下方式私下报告：

1. **GitHub Security Advisories**
   - 访问：https://github.com/sanye66/Educational-Administration-System/security/advisories
   - 点击 "Report a vulnerability"

2. **电子邮件**
   - 发送至：security@educational-system.com（示例邮箱，请替换为实际邮箱）

### 报告内容

请包含以下信息：

- **漏洞类型**：例如 SQL 注入、XSS、CSRF 等
- **影响范围**：受影响的模块和版本
- **复现步骤**：详细的利用方法
- **潜在影响**：可能造成的危害
- **修复建议**：如果有，提供修复方案

### 响应时间

- **确认收到**：1-2 个工作日内
- **初步评估**：3-5 个工作日内
- **修复方案**：根据严重程度，1-4 周内
- **公开披露**：修复发布后

### 致谢

我们会在所有安全公告中感谢负责任的披露者（除非您要求匿名）。

## 安全最佳实践

### 🔐 认证与授权

1. **强密码策略**
   ```yaml
   # 建议在配置中启用
   security:
     password:
       min-length: 8
       require-uppercase: true
       require-lowercase: true
       require-digit: true
       require-special: true
   ```

2. **JWT Token 安全**
   - 使用强密钥（至少 256 位）
   - 设置合理的过期时间（默认 2 小时）
   - 实现 Token 刷新机制
   - 不要在前端存储敏感信息

3. **权限控制**
   - 遵循最小权限原则
   - 定期审查用户权限
   - 及时禁用离职员工账号

### 🗄️ 数据安全

1. **数据库安全**
   ```sql
   -- 使用参数化查询（MyBatis-Plus 已内置）
   -- 避免拼接 SQL
   SELECT * FROM user WHERE username = #{username}
   ```

2. **敏感数据加密**
   - 密码：BCrypt 加密
   - 身份证号、手机号：AES 加密存储
   - API Key：环境变量管理

3. **数据备份**
   - 定期备份数据库
   - 备份文件加密存储
   - 测试恢复流程

### 🌐 网络安全

1. **HTTPS**
   - 生产环境必须启用 HTTPS
   - 使用 TLS 1.2+
   - 定期更新 SSL 证书

2. **防火墙规则**
   ```bash
   # 仅开放必要端口
   - 80/443: HTTP/HTTPS
   - 8080: API Gateway（可选，通过 Nginx 代理）
   - 其他内部端口不对外开放
   ```

3. **CORS 配置**
   ```java
   // 限制允许的域名
   @Configuration
   public class CorsConfig {
       @Bean
       public CorsFilter corsFilter() {
           CorsConfiguration config = new CorsConfiguration();
           config.setAllowedOrigins(List.of("https://yourdomain.com"));
           // 不要使用 "*"
       }
   }
   ```

### 📝 输入验证

1. **服务端验证**
   ```java
   @PostMapping("/user")
   public Result createUser(@Valid @RequestBody UserDTO userDTO) {
       // JSR-303 验证
   }
   
   public class UserDTO {
       @NotBlank(message = "用户名不能为空")
       @Size(min = 3, max = 50)
       private String username;
       
       @Email(message = "邮箱格式不正确")
       private String email;
   }
   ```

2. **XSS 防护**
   - 对用户输入进行 HTML 转义
   - 使用 Content-Security-Policy 头
   - 富文本使用白名单过滤

3. **SQL 注入防护**
   - 使用 MyBatis-Plus 参数化查询
   - 禁止动态拼接 SQL
   - 定期代码审查

### 🔍 日志与监控

1. **审计日志**
   - 记录所有登录尝试
   - 记录敏感操作（删除、修改权限等）
   - 不包含敏感信息（密码、Token）

2. **异常监控**
   - 监控异常频率
   - 设置告警阈值
   - 及时发现攻击行为

3. **速率限制**
   ```java
   // 登录接口限流
   @RateLimit(maxAttempts = 5, timeWindow = 60)
   @PostMapping("/login")
   public Result login(@RequestBody LoginRequest request) {
       // ...
   }
   ```

### 🛡️ 依赖安全

1. **定期更新**
   ```bash
   # 检查依赖漏洞
   mvn dependency-check:check
   
   # 更新依赖
   mvn versions:display-dependency-updates
   ```

2. **漏洞扫描**
   - 集成 Snyk、Dependabot 等工具
   - CI/CD 流水线自动扫描
   - 及时修复高危漏洞

3. **供应链安全**
   - 使用可信的 Maven 仓库
   - 锁定依赖版本
   - 审查新增依赖

### 🐳 容器安全

1. **Docker 最佳实践**
   ```dockerfile
   # 使用非 root 用户
   USER appuser
   
   # 多阶段构建减小镜像
   FROM maven:3.8-openjdk-11 AS build
   FROM openjdk:11-jre-slim
   
   # 定期更新基础镜像
   ```

2. **Kubernetes 安全**
   ```yaml
   securityContext:
     runAsNonRoot: true
     readOnlyRootFilesystem: true
     allowPrivilegeEscalation: false
   ```

3. **Secret 管理**
   - 使用 K8s Secrets
   - 不要硬编码在代码中
   - 启用 Secret 加密

### 🔒 API 安全

1. **认证保护**
   ```java
   @RestController
   @RequestMapping("/api/user")
   public class UserController {
       
       @GetMapping("/{id}")
       @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
       public Result getUser(@PathVariable Long id) {
           // 只有管理员或本人可访问
       }
   }
   ```

2. **请求限流**
   - 防止 DDoS 攻击
   - 保护后端服务
   - 按 IP 和用户限流

3. **API 版本控制**
   - 使用版本号（/api/v1/）
   - 废弃旧版本时提前通知
   - 保持向后兼容

## 已知安全问题

### 当前版本 (1.0.0)

| CVE | 严重程度 | 描述 | 状态 | 修复版本 |
|-----|---------|------|------|---------|
| - | - | 暂无已知高危漏洞 | ✅ 安全 | - |

### 历史问题

我们会在修复后在此处记录重要的安全问题。

## 安全更新流程

### 1. 接收报告

- 通过安全渠道接收漏洞报告
- 确认漏洞的有效性和影响范围

### 2. 评估风险

- 确定严重程度（Critical/High/Medium/Low）
- 评估受影响的用户数量
- 制定修复优先级

### 3. 开发修复

- 创建私有分支
- 开发修复方案
- 内部测试验证

### 4. 发布补丁

- 发布安全更新版本
- 更新 CHANGELOG
- 发布安全公告

### 5. 公开披露

- 在修复发布后公开详细信息
- 感谢报告者
- 提供升级指南

### 6. 后续跟进

- 监控 exploit 出现
- 协助用户升级
- 总结经验教训

## 安全检查清单

部署前请确认：

### 应用层
- [ ] 所有默认密码已修改
- [ ] JWT 密钥已更换
- [ ] 调试接口已禁用
- [ ] 详细错误信息不暴露给前端
- [ ] CORS 配置正确
- [ ] 启用了 HTTPS

### 数据库
- [ ] 使用了强密码
- [ ] 限制了远程访问
- [ ] 启用了审计日志
- [ ] 定期备份策略已配置

### 中间件
- [ ] Nacos 启用了认证
- [ ] Redis 设置了密码
- [ ] RocketMQ 启用了 ACL
- [ ] 所有服务使用了最新版本

### 网络
- [ ] 防火墙规则已配置
- [ ] 仅开放必要端口
- [ ] 内网服务不对外暴露
- [ ] 启用了 WAF（如适用）

### 监控
- [ ] 日志集中收集
- [ ] 异常告警已配置
- [ ] 性能监控已启用
- [ ] 安全事件告警已设置

### 备份
- [ ] 数据库备份已配置
- [ ] 配置文件已备份
- [ ] 恢复流程已测试

## 安全资源

- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [Spring Security 文档](https://spring.io/projects/spring-security)
- [Nacos 安全指南](https://nacos.io/zh-cn/docs/auth.html)
- [Docker 安全最佳实践](https://docs.docker.com/engine/security/)
- [Kubernetes 安全](https://kubernetes.io/docs/concepts/security/)

## 联系方式

- **安全问题报告**: https://github.com/sanye66/Educational-Administration-System/security/advisories
- **一般咨询**: 通过 GitHub Issues

---

**最后更新**: 2026-06-01  
**版本**: 1.0.0
