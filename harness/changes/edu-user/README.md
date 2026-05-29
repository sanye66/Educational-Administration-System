# edu-user 服务变更总结

## 基本信息

| 项目 | 内容 |
|------|------|
| 服务名称 | edu-user |
| 版本 | 1.0.0 |
| 数据库 | edu_user |
| 数据库类型 | MySQL |
| 端口 | 13306 |

## 变更内容

### 数据库表

- **sys_user**: 系统用户表，存储系统用户信息

### 主要字段

- 用户基本信息：username, password, real_name, email, phone, avatar, gender
- 用户类型：user_type (ADMIN/TEACHER/STUDENT)
- 状态管理：status (0-正常 1-禁用 2-锁定)
- 审计字段：create_by, create_time, update_by, update_time, deleted

### 初始化数据

- 默认管理员账号：admin / 123456 (BCrypt加密)

## 相关文件

| 文件 | 说明 |
|------|------|
| db.yml | 数据库信息配置 |
| rollback.sql | 数据库回滚脚本 |
| README.md | 本总结文档 |

## 回滚说明

执行 `rollback.sql` 可清空 sys_user 表数据。如需完全删除表，取消脚本中的 DROP TABLE 注释。
