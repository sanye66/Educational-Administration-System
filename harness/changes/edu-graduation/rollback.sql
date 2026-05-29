-- edu-graduation 服务回滚脚本
-- 版本: 1.0.0
-- 描述: 撤销 edu_graduation 数据库的变更

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE graduation_topic;

-- DROP TABLE IF EXISTS graduation_topic;

SET FOREIGN_KEY_CHECKS = 1;
