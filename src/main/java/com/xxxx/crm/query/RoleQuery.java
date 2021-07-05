package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;

import javax.management.Query;

public class RoleQuery extends BaseQuery {
 //角色名
 private String roleName;

    public RoleQuery() {
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
