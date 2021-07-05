package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dto.TreeModule;
import com.xxxx.crm.mapper.ModuleMapper;
import com.xxxx.crm.mapper.PermissionMapper;
import com.xxxx.crm.vo.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Module,Integer> {
 @Autowired(required = false)
 private ModuleMapper moduleMapper;
 @Autowired(required = false)
 private PermissionMapper permissionMapper;

 /**
  * 角色管理模块下的授权资源显示树
  * 前端为对应用户拥有的权限资源设置为选中回显状态
  * @return
  */
 public List<TreeModule> queryAllModule(Integer roleId){
  //查询获取所有的资源
  List<TreeModule> treeModules = moduleMapper.selectAllModule();
  //查询当前角色的资源
  List<Integer> roleHasMids=  permissionMapper.queryModuleByRoleId(roleId);
  //循环遍历，判断角色拥有那些资源，checked=true,checked=false;
  for (TreeModule module:treeModules) {
   //判断角色拥有的资源id是否包含于遍历到的module的id中，若包含证明该角色拥有这个资源，设置它的选中状态标志位true
   if(roleHasMids.contains(module.getId())){
    module.setChecked(true);
   }
  }
  return  treeModules;
 }

 /**
  * 资源展示列表
  * @return
  */
 public Map<String,Object> queryAllModules(){
  Map<String,Object> map=new HashMap<>();
  //查询所有的数据
  List<Module> mlist = moduleMapper.selectAllModules();
  //准备数据
  map.put("code",0);
  map.put("msg","success");
  map.put("count",mlist.size());
  map.put("data",mlist);
  //返回目标map
  return map;

 }


}
