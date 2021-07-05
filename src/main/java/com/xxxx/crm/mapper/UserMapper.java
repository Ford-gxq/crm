package com.xxxx.crm.mapper;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {

    // 根据用户名查询用户对象
    User queryUserByUserName(String userName);

    //查询所有的销售人员信息
    public List<Map<String,Object>> selectAllSales();

    //插入用户信息，有当前对象的userId
    public int insertUserByReturnKey(User user);


}