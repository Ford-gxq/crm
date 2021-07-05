package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class UserController extends BaseController {
    @Resource
    private UserService userService;

    /**
     * 用户登录
     *
     * @param userName
     * @param userPwd
     * @return
     */
    @RequestMapping("user/login")
    @ResponseBody
    public ResultInfo userLogin(String userName, String userPwd) {
        ResultInfo resultInfo = new ResultInfo();
        // 通过 try catch 捕获 Service 层抛出的异常
//        try {
            // 调用Service层的登录方法，得到返回的用户对象
            UserModel userModel = userService.userLogin(userName, userPwd);
            /**
             * 登录成功后，有两种处理：
             *  1. 将用户的登录信息存入 Session （ 问题：重启服务器，Session 失效，客户端需要重复登录 ）
             *  2. 将用户信息返回给客户端，由客户端（Cookie）保存
             */
            // 将返回的UserModel对象设置到 ResultInfo 对象中
            resultInfo.setResult(userModel);

//        } catch (ParamsException e) {
//            // 自定义异常
//            e.printStackTrace();
//            // 设置状态码和提示信息
//            resultInfo.setCode(e.getCode());
//            resultInfo.setMsg(e.getMsg());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            resultInfo.setCode(500);
//            resultInfo.setMsg("操作失败！");
//        }

        return resultInfo;
    }
  /**
 * 用户密码修改的对应操作--->updateUserPassword 方法实现
 * @param req
 * @param oldPassword
 * @param newPassword
 * @param confirmPassword
 * @return
 */
  @RequestMapping("user/updatePassword")
  @ResponseBody
  public ResultInfo sayPassword(HttpServletRequest req, String oldPassword, String newPassword, String confirmPassword){
      //创建返回信息对象
      ResultInfo resultInfo=new ResultInfo();
      //获取userId, 调用Service层的密码修改方法, 并进行try{}catch(){}捕获Service层可能抛出的异常
      //try{
      int userId = LoginUserUtil.releaseUserIdFromCookie(req);
      //调用Service层的密码修改方法
      userService.updateUserPassword(userId,oldPassword,newPassword,confirmPassword);
/*        }catch (ParamsException pe){
            pe.printStackTrace();
            //初始化对象result
            resultInfo.setMsg(pe.getMsg());
            resultInfo.setCode(pe.getCode());
        }catch (Exception ex){
            ex.printStackTrace();
            resultInfo.setCode(300);
            resultInfo.setMsg("操作失败了");
        }*/
      //返回目标对象
      return resultInfo;
  }

    /**
     * 在 UserController 控制层，添加对应的视图转发方法
     * 前端请求的handler方法用于页面跳转
     * @return
     */
    @RequestMapping("user/toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }

    /**
     * 基本资料的修改
     * @param req
     * @return
     */
    @RequestMapping("user/toSettingPage")
    public String toSettingPage(HttpServletRequest req) {
        //重当前Cookie中获取对象userId
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //根据userId查询用户信息
        User user = userService.selectByPrimaryKey(userId);
        //存储
        req.setAttribute("user", user);
        //转发
        return "user/setting";
    }

    @RequestMapping("user/update")
    @ResponseBody
    public ResultInfo testUpdate(User user) {
        userService.updateByPrimaryKeySelective(user);
        return success("保存信息成功了");
    }
    /**
     *用户管理模块
     * 多条件查询用户数据
     * @param query-->参数对象
     * @return
     */
    @RequestMapping("user/list")
    @ResponseBody
    public Map<String,Object> queryUserByParams(UserQuery query){
        return  userService.queryUserByParams(query);
    }

    /**
     * 进入用户页面
     * @return
     */
    @RequestMapping("user/index")
    public String index(){
        return "user/user";
    }
    /**
     * 添加用户
     * @param user
     * @return
     */
    @RequestMapping("user/save")
    @ResponseBody
    public ResultInfo saveUser(User user) {
        userService.addUser(user);
        return success("用户添加成功！");
    }


    /**
     * 进入用户添加或更新页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("user/addOrUpdateUserPage")
    public String addUserPage(Integer id, Model model){
        if (id != null) {
            //查询用户信息
            User user = userService.selectByPrimaryKey(id);
            //存储
            model.addAttribute("user", user);
        }
        return "user/add_update";
    }
    /**
     * 删除用户操作
     * @param ids
     * @return
     */
    @RequestMapping("user/delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteUserByIds(ids);
        return success("用户记录删除成功");
    }
    /**
     * 查询所有的销售人员
     * @return
     */
    @RequestMapping("user/queryAllSales")
    @ResponseBody
    public List<Map<String, Object>> queryAllSales() {
        return userService.queryAllSales();
    }

}
