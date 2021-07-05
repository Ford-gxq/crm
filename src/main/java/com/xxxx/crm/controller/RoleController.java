package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.dto.TreeModule;
import com.xxxx.crm.query.RoleQuery;
import com.xxxx.crm.service.ModuleService;
import com.xxxx.crm.service.RoleService;
import com.xxxx.crm.vo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController  extends BaseController {
@Autowired
private RoleService roleService;
@Autowired
private ModuleService moduleService;

    /**
     * 查询角色列表
     * @return
     */
    @RequestMapping("queryRoles")
    @ResponseBody
    public List<Map<String,Object>> sayListRole(Integer userId){
        List<Map<String,Object>> mlist=new ArrayList<>();
        return  roleService.queryRoles(userId);
    }

    /**
     * 进入角色管理界面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "role/role";
    }
    /**
     * 多条件查询角色信息
     * @param roleQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> userList(RoleQuery roleQuery){
        return roleService.queryByParamsForTable(roleQuery);
    }

    /**
     * 进入添加和更新页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("addOrUpdateRolePage")
    public String addUserPage(Integer id, Model model){
        if(null !=id){
            model.addAttribute("role",roleService.selectByPrimaryKey(id));
        }
        return "role/add_update";
    }

    /**
     * 添加角色操作
     * @param role
     * @return
     */
@RequestMapping("save")
@ResponseBody
public ResultInfo savaRole(Role role){
  roleService.saveRole(role);
  return success("角色添加成功");
}
    /**
     * 角色更新成功
     * @param role
     * @return
     */
 @RequestMapping("update")
 @ResponseBody
public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success("角色更新成功");
    }
    /**
     * 角色删除操作
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer id){
        roleService.deleteRole(id);
        return success("角色记录删除成功");
    }
    /**
     *角色管理模块的授权按钮---->对应的视图
     * @param roleId
     * @param model
     * @return
     */
    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId,Model model){
        model.addAttribute("roleId",roleId);
        return "role/grant";
    }

    /**
     * 角色管理模块下的授权资源显示树
     *  实现角色拥有的资源回显选中状态
     * @return
     */
    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeModule> TreeAllModule(Integer roleId){
        return moduleService.queryAllModule(roleId);
    }
    /**
     * 为某个用户的某个角色添加权限资源
      */
 @RequestMapping("addGrant")
 @ResponseBody
 public ResultInfo addGrant(Integer[] mids,Integer roleId){
  roleService.addGrant(mids, roleId);
 return  success("权限添加成功");
 }


}
