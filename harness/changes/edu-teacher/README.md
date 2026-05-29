# edu-teacher 服务变更总结

## 基本信息

| 项目 | 内容 |
|------|------|
| 服务名称 | edu-teacher |
| 版本 | 1.0.0 |
| 数据库 | edu_teacher |
| 数据库类型 | MySQL |
| 端口 | 3306 |

## 变更内容

### 数据库表

- **teacher**: 教师表，存储教师详细信息

### 主要字段

- 关联信息：user_id (关联用户表), teacher_no (教师工号)
- 基本信息：department_id, title (职称), education (学历)
- 专业信息：research_direction (研究方向), entry_date (入职日期)
- 审计字段：create_by, create_time, update_by, update_time, deleted

## 相关文件

| 文件 | 说明 |
|------|------|
| db.yml | 数据库信息配置 |
| rollback.sql | 数据库回滚脚本 |
| README.md | 本总结文档 |

## 回滚说明

执行 `rollback.sql` 可清空 teacher 表数据。
