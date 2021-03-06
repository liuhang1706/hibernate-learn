package com.liuhang.hibernate.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * CREATE DATABASE IF NOT EXISTS db_hibernate;
 * CREATE TABLE `cst_customer`(
 * 	`cust_id` BIGINT(32) NOT NULL AUTO_INCREMENT COMMENT '客户编号（主键）',
 * 	`cust_name` VARCHAR(32) NOT NULL COMMENT '客户名称（公司名称）',
 * 	`cust_source` VARCHAR(32) DEFAULT NULL COMMENT '客户信息来源',
 * 	`cust_industry` VARCHAR(32) DEFAULT NULL COMMENT '客户所属行业',
 * 	`cust_level` VARCHAR(32) DEFAULT NULL COMMENT '客户级别',
 * 	`cust_phone` VARCHAR(32) DEFAULT NULL COMMENT '固定电话',
 * 	`cust_mobile` VARCHAR(32) DEFAULT NULL COMMENT '移动电话',
 * 	PRIMARY KEY (`cust_id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
 */
public class CustomerForLinkMan {

    private Long cust_id;
    private String cust_name;
    private String cust_source;
    private String cust_industry;
    private String cust_level;
    private String cust_phone;
    private String cust_mobile;

    // 一个客户对应多个联系人
    // hibernate默认使用set集合
    protected Set<LinkMan> linkManSet = new HashSet<>();

    @Override
    public String toString() {
        return "CustomerForLinkMan{" +
                "cust_id=" + cust_id +
                ", cust_name='" + cust_name + '\'' +
                ", cust_source='" + cust_source + '\'' +
                ", cust_industry='" + cust_industry + '\'' +
                ", cust_level='" + cust_level + '\'' +
                ", cust_phone='" + cust_phone + '\'' +
                ", cust_mobile='" + cust_mobile + '\'' +
                '}';
    }

    public Set<LinkMan> getLinkManSet() {
        return linkManSet;
    }

    public void setLinkManSet(Set<LinkMan> linkManSet) {
        this.linkManSet = linkManSet;
    }

    public Long getCust_id() {
        return cust_id;
    }

    public void setCust_id(Long cust_id) {
        this.cust_id = cust_id;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getCust_source() {
        return cust_source;
    }

    public void setCust_source(String cust_source) {
        this.cust_source = cust_source;
    }

    public String getCust_industry() {
        return cust_industry;
    }

    public void setCust_industry(String cust_industry) {
        this.cust_industry = cust_industry;
    }

    public String getCust_level() {
        return cust_level;
    }

    public void setCust_level(String cust_level) {
        this.cust_level = cust_level;
    }

    public String getCust_phone() {
        return cust_phone;
    }

    public void setCust_phone(String cust_phone) {
        this.cust_phone = cust_phone;
    }

    public String getCust_mobile() {
        return cust_mobile;
    }

    public void setCust_mobile(String cust_mobile) {
        this.cust_mobile = cust_mobile;
    }

    public CustomerForLinkMan() {
    }

    public CustomerForLinkMan(Long cust_id, String cust_name) {
        this.cust_id = cust_id;
        this.cust_name = cust_name;
    }
}
