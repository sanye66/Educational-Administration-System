-- edu-exam 服务回滚脚本
-- 版本: 1.0.0
-- 描述: 撤销 edu_exam 数据库的变更

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE exam_plan;

-- DROP TABLE IF EXISTS exam_plan;

SET FOREIGN_KEY_CHECKS = 1;
