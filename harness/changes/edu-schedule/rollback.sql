-- edu-schedule 服务回滚脚本
-- 版本: 1.0.0
-- 描述: 撤销 edu_schedule 数据库的变更

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE schedule_result;

-- DROP TABLE IF EXISTS schedule_result;

SET FOREIGN_KEY_CHECKS = 1;
