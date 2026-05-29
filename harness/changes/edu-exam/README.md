# edu-exam 服务变更总结

## 基本信息

| 项目 | 内容 |
|------|------|
| 服务名称 | edu-exam |
| 版本 | 1.0.0 |
| 数据库 | edu_exam |
| 数据库类型 | MySQL |
| 端口 | 3306 |

## 变更内容

### 数据库表

- **exam_plan**: 考试安排表，存储考试计划信息

### 主要字段

- 关联信息：course_id (课程ID), classroom_id (考场教室ID)
- 学期信息：academic_year (学年), semester (学期)
- 考试信息：exam_time (考试时间), duration (时长), exam_type (考试类型)
- 状态管理：status (SCHEDULED/IN_PROGRESS/COMPLETED/CANCELLED)
- 审计字段：create_by, create_time, update_by, update_time, deleted

## 相关文件

| 文件 | 说明 |
|------|------|
| db.yml | 数据库信息配置 |
| rollback.sql | 数据库回滚脚本 |
| README.md | 本总结文档 |

## 回滚说明

执行 `rollback.sql` 可清空 exam_plan 表数据。
