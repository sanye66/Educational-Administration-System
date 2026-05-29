# edu-schedule 服务变更总结

## 基本信息

| 项目 | 内容 |
|------|------|
| 服务名称 | edu-schedule |
| 版本 | 1.0.0 |
| 数据库 | edu_schedule |
| 数据库类型 | MySQL |
| 端口 | 3306 |

## 变更内容

### 数据库表

- **schedule_result**: 排课结果表，存储课程安排信息

### 主要字段

- 关联信息：course_id (课程ID), teacher_id (教师ID), classroom_id (教室ID)
- 学期信息：academic_year (学年), semester (学期)
- 时间安排：day_of_week (星期几), start_section (开始节次), duration_section (持续节次)
- 状态管理：status (DRAFT/CONFIRMED/PUBLISHED)
- 审计字段：create_by, create_time, update_by, update_time, deleted

## 相关文件

| 文件 | 说明 |
|------|------|
| db.yml | 数据库信息配置 |
| rollback.sql | 数据库回滚脚本 |
| README.md | 本总结文档 |

## 回滚说明

执行 `rollback.sql` 可清空 schedule_result 表数据。
