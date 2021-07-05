package com.xxxx.crm.mapper;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.dto.TreeModule;
import com.xxxx.crm.vo.Module;

import java.util.List;

public interface ModuleMapper  extends BaseMapper<Module,Integer> {
    //查询所有的资源，roleId
    //id,name,pid;
    public List<TreeModule> selectAllModule();
    //查询所有的资源
    List<Module> selectAllModules();
}