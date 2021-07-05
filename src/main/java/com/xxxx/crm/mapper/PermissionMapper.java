package com.xxxx.crm.mapper;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {
    Permission selectByPrimaryKey(Integer id);
    //根据角色id,查询资源的数量集合
    int countPermissionByRoleId(Integer roleId);
    //根据roleId删除其所有的权限信息
    int deletePermissionByRoleId(Integer roleId);
    //查询角色拥有的资源
    List<Integer> queryModuleByRoleId(Integer roleId);
    //查询用户的权限码
    List<String> queryPermissionByUserId(int userId);
}

