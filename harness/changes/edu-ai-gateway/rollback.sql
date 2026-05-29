-- edu-ai-gateway 服务回滚脚本
-- 版本: 1.0.0
-- 描述: 撤销 edu_ai_gateway 数据库的变更

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE usage_record;
TRUNCATE TABLE ai_model_config;

-- DROP TABLE IF EXISTS usage_record;
-- DROP TABLE IF EXISTS ai_model_config;

SET FOREIGN_KEY_CHECKS = 1;
