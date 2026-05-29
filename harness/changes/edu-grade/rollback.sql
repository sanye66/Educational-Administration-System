-- edu-grade 服务回滚脚本
-- 版本: 1.0.0
-- 描述: 撤销 edu_grade 数据库的变更

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE grade;

-- DROP TABLE IF EXISTS grade;

SET FOREIGN_KEY_CHECKS = 1;
