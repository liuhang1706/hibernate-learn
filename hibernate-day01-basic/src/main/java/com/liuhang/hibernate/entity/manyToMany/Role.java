package com.liuhang.hibernate.entity.manyToMany;

import java.util.HashSet;
import java.util.Set;

/**
 * CREATE TABLE `sys_role`(
 * 	`role_id` BIGINT(32) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
 * 	`role_name` VARCHAR(32) NOT NULL COMMENT '角色账号',
 * 	`role_memo` VARCHAR(128) DEFAULT NULL COMMENT '备注',
 * 	PRIMARY KEY (`role_id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
 */
public class Role {

    private Long role_id;
    private String role_name;
    private String role_memo;

    /**
     * 一个角色被多个用户选择
     */
    private Set<User> users = new HashSet<>();

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getRole_memo() {
        return role_memo;
    }

    public void setRole_memo(String role_memo) {
        this.role_memo = role_memo;
    }
}
