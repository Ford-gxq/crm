package com.xxxx.crm.mapper;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {
      //查询所有的角色
    public List<Map<String,Object>> selectAllRoles(Integer userId);

    Role selectRoleByName(String roleName);
}