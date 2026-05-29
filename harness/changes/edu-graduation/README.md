# edu-graduation 服务变更总结

## 基本信息

| 项目 | 内容 |
|------|------|
| 服务名称 | edu-graduation |
| 版本 | 1.0.0 |
| 数据库 | edu_graduation |
| 数据库类型 | MySQL |
| 端口 | 3306 |

## 变更内容

### 数据库表

- **graduation_topic**: 毕设题目表，存储毕业设计题目信息

### 主要字段

- 题目信息：title (毕设题目), description (描述)
- 关联信息：teacher_id (指导教师ID), student_id (学生ID)
- 学年信息：academic_year (学年)
- 难度级别：difficulty (EASY/MEDIUM/HARD)
- 状态管理：status (AVAILABLE/SELECTED/IN_PROGRESS/COMPLETED)
- 审计字段：create_by, create_time, update_by, update_time, deleted

## 相关文件

| 文件 | 说明 |
|------|------|
| db.yml | 数据库信息配置 |
| rollback.sql | 数据库回滚脚本 |
| README.md | 本总结文档 |

## 回滚说明

执行 `rollback.sql` 可清空 graduation_topic 表数据。
