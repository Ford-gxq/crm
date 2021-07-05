package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.mapper.ModuleMapper;
import com.xxxx.crm.mapper.PermissionMapper;
import com.xxxx.crm.mapper.RoleMapper;
import com.xxxx.crm.query.RoleQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Permission;
import com.xxxx.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service
public class RoleService extends BaseService<Role,Integer> {
@Autowired(required = false)
private RoleMapper roleMapper;
@Autowired(required = false)
private PermissionMapper permissionMapper;
@Autowired(required = false)
private ModuleMapper moduleMapper;

    /**
     *营销机会管理模块-->行内编辑指派人
     * @param userId
     * @return
     */
public List<Map<String,Object>> queryRoles(Integer userId){
        return roleMapper.selectAllRoles(userId);
}
    /**
     * 前端角色信息表数据表格的渲染---->多条件查询角色信息
     * @param query
     * @return
     */
    public Map<String,Object> queryByParamsForTable(RoleQuery query){
    //创建一个map集合用于存放返回的数据格式的参数和数据
    Map<String, Object> map = new HashMap<>();
    //分页初始化，getPage当前的页码，getLimit-->每页显示的条数
    PageHelper.startPage(query.getPage(), query.getLimit());
    //从数据库中依据查询参数对象获取销售机会的集合list
    List<Role>  roles= roleMapper.selectByParams(query);
    //创建pageInfo对象，分页信息对象，再将查询所得的销售机会集合注入到分页信息对象中
    PageInfo<Role> pageInfo = new PageInfo<>(roles);
    //将查询所得的数据和参数存入map集合
    map.put("code",0);
    map.put("msg", "success");
    map.put("count", pageInfo.getTotal());//总记录数
    map.put("data", pageInfo.getList());//角色数据集合
    return map;
}
    /**
     * 角色的添加
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
public void saveRole(Role role){
 //角色非空验证
    AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名");
//调用roleMapper的queryRoleByRoleName方法查询角色对象名
    Role temp = roleMapper.selectRoleByName(role.getRoleName());
    //判断角色是否已经存在
    AssertUtil.isTrue(null!=temp,"角色已经存在");
   //设置角色的默认值
    role.setIsValid(1);
    role.setCreateDate(new Date());
    role.setUpdateDate(new Date());
    //添加操作是否成功
    AssertUtil.isTrue(insertSelective(role)<1,"角色添加失败");

}

    /**
     * 角色的修改
     * @param role
     */
 public void updateRole(Role role){
     //非空校验，首先判断待更新的角色数据是否存在，再判断角色名是否为空
     AssertUtil.isTrue(null==role.getId()||null==selectByPrimaryKey(role.getId()),"待修改的角色数据不存在");
     AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入要更新的角色数据名");
     //更新的角色与数据库中用户的原角色不能相同
     Role temp = roleMapper.selectRoleByName(role.getRoleName());
     AssertUtil.isTrue(null !=temp && !(temp.getId().equals(role.getId())),"该角色已存在!");
     //设置更新的时间
     role.setUpdateDate(new Date());
     //执行操作的结果是否成功
     AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)<1,"角色更新失败");
 }
    /**
     * 角色删除
     * @param roleId
     */
    public void deleteRole(Integer roleId){
        Role temp =selectByPrimaryKey(roleId);
        AssertUtil.isTrue(null==roleId||null==temp,"待删除的记录不存在!");
        temp.setIsValid(0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(temp)<1,"角色记录删除失败!");
    }

    /**
     * 通过Ztree组件，为用户添加权限记录
     * @param mids
     * @param roleId
     */
    public void addGrant(Integer[] mids, Integer roleId) {
        /*
         * 核心表-t_permission  t_role(校验角色存在)
         *   如果角色存在原始权限  删除角色原始权限
         *     然后添加角色新的权限 批量添加权限记录到t_permission
         */
   //验证待授权的角色是否存在
        Role roletmp = roleMapper.selectByPrimaryKey(roleId);
   AssertUtil.isTrue(null==roleId || null==roletmp,"待授权的角色不存在！");
   int count = permissionMapper.countPermissionByRoleId(roleId);
   if(count>0){
   AssertUtil.isTrue(permissionMapper.deletePermissionByRoleId(roleId)<count,"权限分配失败");

   }
   //判断mids资源Id数组是否为空，长度是否大于0
   if (null !=mids && mids.length>0){
   //创建一个权限ArrayList集合用于存放Permission对象
       ArrayList<Permission>  permissions= new ArrayList<>();
   for (Integer mid :mids){
       Permission permission=new Permission();
       //设置创建权限的日期和更新日期
       permission.setCreateDate(new Date());
       permission.setUpdateDate(new Date());
       permission.setModuleId(mid);
       permission.setRoleId(roleId);
       //根据资源id查询资源为它设置操作权限码
       permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());
       permissions.add(permission);
    }
       permissionMapper.insertBatch(permissions);
   }

    }




}
