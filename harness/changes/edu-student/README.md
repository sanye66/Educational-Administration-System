# edu-student 服务变更总结

## 基本信息

| 项目 | 内容 |
|------|------|
| 服务名称 | edu-student |
| 版本 | 1.0.0 |
| 数据库 | edu_student |
| 数据库类型 | MySQL |
| 端口 | 3306 |

## 变更内容

### 数据库表

- **student**: 学生表，存储学生详细信息

### 主要字段

- 关联信息：user_id (关联用户表), student_no (学号)
- 院系信息：department_id, major_id, class_id
- 学籍信息：enrollment_year (入学年份), schooling_length (学制年限), status (学籍状态)
- 审计字段：create_by, create_time, update_by, update_time, deleted

## 相关文件

| 文件 | 说明 |
|------|------|
| db.yml | 数据库信息配置 |
| rollback.sql | 数据库回滚脚本 |
| README.md | 本总结文档 |

## 回滚说明

执行 `rollback.sql` 可清空 student 表数据。
