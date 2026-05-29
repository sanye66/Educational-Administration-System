-- edu-teacher 服务回滚脚本
-- 版本: 1.0.0
-- 描述: 撤销 edu_teacher 数据库的变更

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE teacher;

-- DROP TABLE IF EXISTS teacher;

SET FOREIGN_KEY_CHECKS = 1;
