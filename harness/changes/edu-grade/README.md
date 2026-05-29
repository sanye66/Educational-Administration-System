# edu-grade 服务变更总结

## 基本信息

| 项目 | 内容 |
|------|------|
| 服务名称 | edu-grade |
| 版本 | 1.0.0 |
| 数据库 | edu_grade |
| 数据库类型 | MySQL |
| 端口 | 3306 |

## 变更内容

### 数据库表

- **grade**: 成绩表，存储学生成绩信息

### 主要字段

- 关联信息：student_id (学生ID), course_id (课程ID), teacher_id (教师ID)
- 学期信息：academic_year (学年), semester (学期)
- 成绩信息：regular_score (平时成绩), midterm_score (期中成绩), final_score (期末成绩), total_score (总评成绩)
- 绩点：gpa (绩点)
- 状态管理：status (DRAFT/CONFIRMED/ARCHIVED)
- 审计字段：create_by, create_time, update_by, update_time, deleted

### 索引

- idx_student_id: student_id
- idx_course_id: course_id

## 相关文件

| 文件 | 说明 |
|------|------|
| db.yml | 数据库信息配置 |
| rollback.sql | 数据库回滚脚本 |
| README.md | 本总结文档 |

## 回滚说明

执行 `rollback.sql` 可清空 grade 表数据。
