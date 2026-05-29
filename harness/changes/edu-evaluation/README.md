# edu-evaluation 服务变更总结

## 基本信息

| 项目 | 内容 |
|------|------|
| 服务名称 | edu-evaluation |
| 版本 | 1.0.0 |
| 数据库 | edu_evaluation |
| 数据库类型 | MySQL |
| 端口 | 3306 |

## 变更内容

### 数据库表

- **evaluation_record**: 教学评价表，存储学生对课程的评价信息

### 主要字段

- 关联信息：student_id (学生ID), course_id (课程ID), teacher_id (教师ID)
- 学期信息：academic_year (学年), semester (学期)
- 评分维度：attitude_score (教学态度), content_score (教学内容), method_score (教学方法), effect_score (教学效果)
- 总评：total_score (总评)
- 文字评价：comment (文字评价)
- 状态管理：status (DRAFT/SUBMITTED)
- 审计字段：create_by, create_time, update_by, update_time, deleted

## 相关文件

| 文件 | 说明 |
|------|------|
| db.yml | 数据库信息配置 |
| rollback.sql | 数据库回滚脚本 |
| README.md | 本总结文档 |

## 回滚说明

执行 `rollback.sql` 可清空 evaluation_record 表数据。
