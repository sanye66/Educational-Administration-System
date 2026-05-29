# edu-course 服务变更总结

## 基本信息

| 项目 | 内容 |
|------|------|
| 服务名称 | edu-course |
| 版本 | 1.0.0 |
| 数据库 | edu_course |
| 数据库类型 | MySQL |
| 端口 | 3306 |

## 变更内容

### 数据库表

- **course**: 课程表，存储课程信息

### 主要字段

- 课程信息：course_no (课程编号), course_name (课程名称), description (课程描述)
- 学分学时：credit (学分), class_hour (学时)
- 容量管理：max_capacity (最大容量), current_count (当前选课人数)
- 学期信息：academic_year (学年), semester (学期)
- 关联信息：category_id, teacher_id
- 状态管理：status (DRAFT/PUBLISHED/CLOSED)
- 审计字段：create_by, create_time, update_by, update_time, deleted

## 相关文件

| 文件 | 说明 |
|------|------|
| db.yml | 数据库信息配置 |
| rollback.sql | 数据库回滚脚本 |
| README.md | 本总结文档 |

## 回滚说明

执行 `rollback.sql` 可清空 course 表数据。
