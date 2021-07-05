package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.service.PermissionService;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;
    @Autowired
    private PermissionService permissionService;

    /**
     * 登录页面
     */
    @RequestMapping("index")
    public String index() {
        return "index";
    }

    /**
     * 欢迎页面面
     */
    @RequestMapping("welcome")
    public String welcome() {
        return "welcome";
    }

    /**
     * 后台页面--->主页面显示用户名信息
     * 在 IndexController控制器中，main 方法转发时，查询登录用户信息并放置到 request域。
     */
    /*@RequestMapping("main")
    public String main(HttpServletRequest request) {
        // 通过工具类，从cookie中获取userId
        int userId = LoginUserUtil.releaseUserIdFromCookie(request);
        System.out.println(userId);
        // 调用对应Service层的方法，通过userId主键查询用户对象
        User user = userService.selectByPrimaryKey(userId);
        System.out.println(user);
        // 将用户对象设置到request作用域中
        request.setAttribute("user", user);
        //页面跳转
        return "main";
    }*/

    @RequestMapping("main")
    public String main(HttpServletRequest request){
        //获取Cookie,UserId
        int userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //根据用户userId查询用户信息
        User user = userService.selectByPrimaryKey(userId);
        //存储数据
        request.setAttribute("user",user);
        //查询当前用户拥有的权限
        List<String> permissions= permissionService.queryPermissionByRoleByUserId(userId);
        //存储到session
        request.getSession().setAttribute("permissions",permissions);
        //跳转
        return "main";
    }

}
