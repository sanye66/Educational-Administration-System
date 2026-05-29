# edu-classroom 服务变更总结

## 基本信息

| 项目 | 内容 |
|------|------|
| 服务名称 | edu-classroom |
| 版本 | 1.0.0 |
| 数据库 | edu_classroom |
| 数据库类型 | MySQL |
| 端口 | 3306 |

## 变更内容

### 数据库表

- **classroom**: 教室表，存储教室资源信息

### 主要字段

- 教室信息：room_no (教室编号), room_name (教室名称)
- 位置信息：building (所属建筑), floor (楼层)
- 容量设备：capacity (容量), equipment (设备描述)
- 类型状态：room_type (教室类型), status (AVAILABLE/OCCUPIED/MAINTENANCE)
- 审计字段：create_by, create_time, update_by, update_time, deleted

## 相关文件

| 文件 | 说明 |
|------|------|
| db.yml | 数据库信息配置 |
| rollback.sql | 数据库回滚脚本 |
| README.md | 本总结文档 |

## 回滚说明

执行 `rollback.sql` 可清空 classroom 表数据。
