# 贡献指南 (Contributing Guide)

感谢您对本项目的关注！我们欢迎任何形式的贡献，包括代码、文档、测试、问题报告等。

## 📋 目录

- [行为准则](#行为准则)
- [如何贡献](#如何贡献)
- [开发流程](#开发流程)
- [代码规范](#代码规范)
- [提交规范](#提交规范)
- [Pull Request 指南](#pull-request-指南)
- [问题报告](#问题报告)

## 行为准则

本项目采用开放和友好的社区原则。请尊重所有参与者，营造积极的协作环境。

## 如何贡献

### 1. 报告 Bug

如果您发现了 Bug，请创建一个 Issue 并包含以下信息：

- **清晰的标题**：简明扼要地描述问题
- **环境信息**：操作系统、JDK 版本、数据库版本等
- **复现步骤**：详细的操作步骤
- **预期行为**：您期望发生什么
- **实际行为**：实际发生了什么
- **相关日志**：错误日志或堆栈跟踪
- **截图**：如果适用，提供截图

### 2. 提出新功能建议

如果您有新功能的想法，请先创建一个 Issue 讨论：

- **功能描述**：清晰描述新功能
- **使用场景**：为什么需要这个功能
- **实现思路**：如果有，提供可能的实现方案
- **替代方案**：是否考虑过其他解决方案

### 3. 提交代码

#### 前置条件

- JDK 11+
- Maven 3.6+
- Git
- IDE（推荐 IntelliJ IDEA）

#### 开发流程

1. **Fork 仓库**
   ```bash
   # 在 GitHub 上点击 Fork 按钮
   ```

2. **克隆仓库**
   ```bash
   git clone https://github.com/YOUR_USERNAME/Educational-Administration-System.git
   cd Educational-Administration-System
   ```

3. **添加上游仓库**
   ```bash
   git remote add upstream https://github.com/sanye66/Educational-Administration-System.git
   ```

4. **创建分支**
   ```bash
   # 同步主分支
   git checkout main
   git pull upstream main
   
   # 创建特性分支
   git checkout -b feature/your-feature-name
   ```

5. **开发功能**
   - 编写代码
   - 添加测试
   - 更新文档

6. **提交更改**
   ```bash
   git add .
   git commit -m "feat: add your feature description"
   ```

7. **推送到您的 Fork**
   ```bash
   git push origin feature/your-feature-name
   ```

8. **创建 Pull Request**
   - 访问您的 Fork 仓库
   - 点击 "Compare & pull request"
   - 填写 PR 描述
   - 提交 PR

## 代码规范

### Java 代码规范

请参考 `harness/rules/` 目录中的详细规范：

- [命名规范](harness/rules/命名规范.md)
- [接口调用规范](harness/rules/接口调用规范.md)
- [异常处理规范](harness/rules/异常处理规范.md)
- [Bean 注入规范](harness/rules/Bean注入规范.md)
- [缓存问题](harness/rules/缓存问题.md)

### 基本原则

1. **命名规范**
   - 类名：大驼峰（PascalCase）
   - 方法名：小驼峰（camelCase）
   - 常量：全大写，下划线分隔
   - 包名：全小写

2. **注释规范**
   - 类和公共方法必须有 Javadoc
   - 复杂逻辑需要行内注释
   - 使用中文注释

3. **代码格式**
   - 使用 4 空格缩进
   - 每行不超过 120 字符
   - 方法长度不超过 80 行

4. **异常处理**
   - 不要捕获通用 Exception
   - 使用自定义业务异常
   - 记录完整的异常信息

### 前端代码规范

1. **TypeScript**
   - 严格模式
   - 明确的类型定义
   - 避免使用 any

2. **Vue 组件**
   - 使用 Composition API
   - 组件名称使用 PascalCase
   - Props 必须有类型和默认值

3. **样式**
   - 使用 scoped 样式
   - 遵循 BEM 命名规范
   - 避免使用 !important

## 提交规范

我们使用 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

### 提交消息格式

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Type 类型

- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档更新
- `style`: 代码格式（不影响功能）
- `refactor`: 重构
- `perf`: 性能优化
- `test`: 测试相关
- `chore`: 构建过程或辅助工具变动
- `ci`: CI 配置更改

### Scope 范围

可选，指明影响的模块：
- `user`: 用户模块
- `course`: 课程模块
- `gateway`: 网关
- `auth`: 认证
- `common`: 公共模块
- 等等...

### Subject 主题

- 简短描述
- 使用祈使句
- 首字母小写
- 不以句号结尾

### Body 正文

可选，详细描述改动的原因和方式。

### Footer 脚注

可选，包含：
- 不兼容的改动（BREAKING CHANGE）
- 关闭的 Issue（Closes #123）

### 示例

```bash
# 新功能
git commit -m "feat(course): add course search functionality"

# Bug 修复
git commit -m "fix(auth): resolve JWT token expiration issue"

# 文档更新
git commit -m "docs: update API documentation for user endpoints"

# 重构
git commit -m "refactor(grade): simplify grade calculation logic"

# 带正文的提交
git commit -m "feat(selection): implement concurrent selection control

Add Redis-based distributed lock to prevent race conditions
during high-concurrency course selection.

Closes #456"
```

## Pull Request 指南

### PR 标题

使用与提交消息相同的格式：`type(scope): description`

### PR 描述模板

```markdown
## 描述
简要描述此 PR 的目的

## 类型
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## 相关问题
Closes #123

## 实现细节
- 详细说明 1
- 详细说明 2

## 测试
- [ ] 单元测试通过
- [ ] 集成测试通过
- [ ] 手动测试完成

## 截图（如适用）
添加前后对比截图

## 检查清单
- [ ] 代码遵循项目规范
- [ ] 添加了必要的测试
- [ ] 更新了相关文档
- [ ] 没有合并冲突
- [ ] CI 检查通过
```

### PR 审查流程

1. **自动检查**
   - CI 流水线运行
   - 代码质量检查
   - 测试覆盖率检查

2. **人工审查**
   - 至少一名维护者审查
   - 代码质量和规范性
   - 功能正确性

3. **反馈与修改**
   - 根据审查意见修改
   - 重新提交审查

4. **合并**
   - 审查通过后合并
   - 删除特性分支

## 测试要求

### 单元测试

- 新增代码必须有单元测试
- 保持测试覆盖率 > 70%
- 测试边界条件和异常情况

### 集成测试

- 关键业务流程需要集成测试
- 模拟外部依赖
- 验证服务间交互

### 运行测试

```bash
# 运行所有测试
mvn test

# 运行特定模块测试
mvn test -pl edu-services/edu-user

# 生成测试报告
mvn surefire-report:report
```

## 文档更新

如果您的改动影响到了文档，请同时更新：

- README.md
- API 文档
- 配置说明
- 部署指南
- 相关 Wiki

## 发布流程

### 版本号规范

使用 [语义化版本](https://semver.org/lang/zh-CN/)：`MAJOR.MINOR.PATCH`

- **MAJOR**: 不兼容的 API 变更
- **MINOR**: 向后兼容的功能新增
- **PATCH**: 向后兼容的问题修正

### 发布步骤

1. 更新版本号
2. 更新 CHANGELOG
3. 创建 Release Tag
4. 构建并发布
5. 更新文档

## 常见问题

### Q: 我的 PR 多久会被审查？

A: 通常在 1-3 个工作日内会有回应。紧急问题可以 @ 维护者。

### Q: 如何同步上游的最新代码？

```bash
git checkout main
git pull upstream main
git checkout feature/your-branch
git rebase main
```

### Q: 如何处理合并冲突？

```bash
# 解决冲突后
git add .
git rebase --continue
git push -f origin feature/your-branch
```

### Q: 我可以一次提交多个功能吗？

A: 不建议。每个 PR 应该只关注一个功能或修复，便于审查和回滚。

## 联系方式

- **Issues**: [GitHub Issues](https://github.com/sanye66/Educational-Administration-System/issues)
- **Discussions**: [GitHub Discussions](https://github.com/sanye66/Educational-Administration-System/discussions)

## 致谢

感谢所有为本项目做出贡献的开发者！

---

**最后更新**: 2026-06-01
