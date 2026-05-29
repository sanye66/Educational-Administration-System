-- edu-user 服务回滚脚本
-- 版本: 1.0.0
-- 描述: 撤销 edu_user 数据库的变更

-- 关闭外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- 删除数据（保留表结构，仅清空数据）
-- 如需完全删除表，请使用 DROP TABLE 语句

-- 清空用户表数据
TRUNCATE TABLE sys_user;

-- 如需完全删除表，取消下面注释
-- DROP TABLE IF EXISTS sys_user;

-- 开启外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- 回滚完成
