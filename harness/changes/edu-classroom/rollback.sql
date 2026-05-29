-- edu-classroom 服务回滚脚本
-- 版本: 1.0.0
-- 描述: 撤销 edu_classroom 数据库的变更

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE classroom;

-- DROP TABLE IF EXISTS classroom;

SET FOREIGN_KEY_CHECKS = 1;
