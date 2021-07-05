package com.xxxx.crm.model;
/**
 *用户登录
 *  1. 验证参数
 *         姓名 非空判断
 *         密码 非空判断
 *     2. 根据用户名，查询用户对象
 *     3. 判断用户是否存在
 *         用户对象为空，记录不存在，方法结束
 *     4. 用户对象不为空
 *         用户存在，校验密码
 *             密码不正确，方法结束
 *     5. 密码正确
 *            用户登录成功，返回用户的相关信息 （定义UserModel类，返回用户某些信息）
 */
public class UserModel {
    private String userIdStr;
    private String userName;
    private String trueName;

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }
}