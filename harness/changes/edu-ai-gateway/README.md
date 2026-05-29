# edu-ai-gateway 服务变更总结

## 基本信息

| 项目 | 内容 |
|------|------|
| 服务名称 | edu-ai-gateway |
| 版本 | 1.0.0 |
| 数据库 | edu_ai_gateway |
| 数据库类型 | MySQL |
| 端口 | 3306 |

## 变更内容

### 数据库表

- **ai_model_config**: AI模型配置表，存储AI模型配置信息
- **usage_record**: AI使用记录表，存储API调用记录

### ai_model_config 主要字段

- 模型信息：model_name (模型名称), model_type (模型类型)
- API配置：api_endpoint (API端点), api_key (API密钥)
- 参数设置：max_tokens (最大令牌数), temperature (温度参数)
- 状态管理：status (ACTIVE/INACTIVE)
- 审计字段：create_by, create_time, update_by, update_time, deleted

### usage_record 主要字段

- 关联信息：user_id (用户ID), model_id (模型ID)
- 用量统计：prompt_tokens (提示令牌数), completion_tokens (完成令牌数), total_tokens (总令牌数)
- 时间记录：request_time (请求时间), response_time (响应时间)
- 状态：status (SUCCESS/FAILED)
- 创建时间：create_time

## 相关文件

| 文件 | 说明 |
|------|------|
| db.yml | 数据库信息配置 |
| rollback.sql | 数据库回滚脚本 |
| README.md | 本总结文档 |

## 回滚说明

执行 `rollback.sql` 可清空 usage_record 和 ai_model_config 表数据。
