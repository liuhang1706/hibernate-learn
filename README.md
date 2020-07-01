建表语句
```sql
CREATE DATABASE IF NOT EXISTS db_hibernate;
CREATE TABLE `cst_customer`(
	`cust_id` BIGINT(32) NOT NULL AUTO_INCREMENT COMMENT '客户编号（主键）',
	`cust_name` VARCHAR(32) NOT NULL COMMENT '客户名称（公司名称）',
	`cust_source` VARCHAR(32) DEFAULT NULL COMMENT '客户信息来源',
	`cust_industry` VARCHAR(32) DEFAULT NULL COMMENT '客户所属行业',
	`cust_level` VARCHAR(32) DEFAULT NULL COMMENT '客户级别',
	`cust_phone` VARCHAR(32) DEFAULT NULL COMMENT '固定电话',
	`cust_mobile` VARCHAR(32) DEFAULT NULL COMMENT '移动电话',
	PRIMARY KEY (`cust_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
CREATE TABLE `cst_linkman`(
	`lkm_id` BIGINT(32) NOT NULL AUTO_INCREMENT COMMENT '联系人编号（主键）',
	`lkm_name` VARCHAR(16) DEFAULT NULL COMMENT '联系人姓名',
	`lkm_cust_id` BIGINT(32) DEFAULT NULL COMMENT '客户ID',
	`lkm_gender` CHAR(1) DEFAULT NULL COMMENT '联系人性别',
	`lkm_phone` VARCHAR(16) DEFAULT NULL COMMENT '联系人办公电话',
	`lkm_mobile` VARCHAR(16) DEFAULT NULL COMMENT '联系人手机',
	`lkm_email` VARCHAR(64) DEFAULT NULL COMMENT '联系人邮箱',
	`lkm_qq` VARCHAR(16) DEFAULT NULL COMMENT '联系人qq',
	`lkm_position` VARCHAR(16) DEFAULT NULL COMMENT '联系人职位',
	`lkm_memo` VARCHAR(512) DEFAULT NULL COMMENT '联系人备注',
	PRIMARY KEY (`lkm_id`),
	KEY `FK_cst_linkman_lkm_cust_id` (`lkm_cust_id`),
	CONSTRAINT `FK_cst_linkman_lkm_cust_id` FOREIGN KEY (`lkm_cust_id`) REFERENCES `cst_customer` (`cust_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `sys_user` ;
CREATE TABLE `sys_user`(
	`user_id` BIGINT(32) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
	`user_code` VARCHAR(32) DEFAULT NULL COMMENT '用户账号',
	`user_name` VARCHAR(64) NOT NULL COMMENT '用户名称',
	`user_password` VARCHAR(32) DEFAULT NULL COMMENT '用户密码',
	`user_state` CHAR(1) DEFAULT NULL COMMENT '1:正常；0：暂停',
	PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `sys_role` ;
CREATE TABLE `sys_role`(
	`role_id` BIGINT(32) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
	`role_name` VARCHAR(32) NOT NULL COMMENT '角色账号',
	`role_memo` VARCHAR(128) DEFAULT NULL COMMENT '备注',
	PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS `sys_user_role` ;
CREATE TABLE `sys_user_role`(
	`role_id` BIGINT(32) NOT NULL COMMENT '角色ID',
	`user_id` BIGINT(32) NOT NULL COMMENT '用户ID',
	PRIMARY KEY (`role_id`,`user_id`),
	CONSTRAINT `FK_user_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT `FK_user_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```