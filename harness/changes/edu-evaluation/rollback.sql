-- edu-evaluation 服务回滚脚本
-- 版本: 1.0.0
-- 描述: 撤销 edu_evaluation 数据库的变更

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE evaluation_record;

-- DROP TABLE IF EXISTS evaluation_record;

SET FOREIGN_KEY_CHECKS = 1;
