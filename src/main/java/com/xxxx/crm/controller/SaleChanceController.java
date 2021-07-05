package com.xxxx.crm.controller;
import com.xxxx.crm.annotation.RequiredPermission;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    //注入SaleChanceService
    @Resource
    private SaleChanceService saleChanceService;
    @Resource
    private UserService userService;
    /**
     * 多条件分页查询营销机会
     * @param saleChanceQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    @RequiredPermission(code = "101001")
    public Map<String,Object> querySaleChancesByParams(Integer flag,HttpServletRequest request,SaleChanceQuery saleChanceQuery){
        if(null !=flag &&flag==1){
            // 查询分配给当前登录用户 营销记录
            saleChanceQuery.setAssignMan(LoginUserUtil.releaseUserIdFromCookie(request));
        }
        //查询
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }
   /**
    * 进入营销机会页面
    * @return
    */
   @RequestMapping("index")
   public String index () {
       return "saleChance/sale_chance";
   }
  /**
   * 添加营销机会数据
   * @param request
   * @param saleChance
   * @return
   */
  @RequestMapping("save")
  @ResponseBody
  public ResultInfo saveSaleChance(HttpServletRequest request, SaleChance saleChance){

      // 获取用户ID
      int userId= LoginUserUtil.releaseUserIdFromCookie(request);
      // 调用userService的 selectByPrimaryKey（id）方法查询,获取用户的真实姓名
      String trueName = userService.selectByPrimaryKey(userId).getTrueName();
      // 设置营销机会的创建人
      saleChance.setCreateMan(trueName);
     // 添加营销机会的数据
     saleChanceService.saveSaleChance(saleChance);
    return success("数据添加成功");

  }
/**
 * 机会数据添加与更新页面视图转发
 * @param id
 * @param model
 * @return
 */
@RequestMapping("addOrUpdateSaleChancePage")
public String addOrUpdateSaleChancePage(Integer id, Model model){
    //修改和添加主要的区别是表单中是否有ID,有id修改操作，否则添加
    if (id != null) {
        //查询销售机会对象
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
        //存储
        model.addAttribute("saleChance", saleChance);
    }
    return "saleChance/add_update";

}
    /**
     *营销机会数据的修改(更新)
     * @param saleChance
     * @param req
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo SaleChanceToUpdate(SaleChance saleChance, HttpServletRequest req) {
        //修改
        saleChanceService.updateByPrimaryKeySelective(saleChance);
        return success("营销机会添加成功");
    }

    /**
     * 营销机会数据的删除(更新)
     * @param ids
     * @return
     */
    @RequestMapping("dels")
    @ResponseBody
    public ResultInfo SaleChanceToDels(Integer[] ids) {
        System.out.println(Arrays.toString(ids)+"<<<<<controller");
        saleChanceService.removeSaleChance(ids);
        return success("批量删除营销机会成功");
    }
    /**
     * 删除营销机会数据
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance (Integer[] ids) {
        // 删除营销机会的数据
        saleChanceService.deleteBatch(ids);
        return success("营销机会数据删除成功！");
    }

    /**
     * 查询所有的销售人员
     * @return
     */
    @RequestMapping("querySales")
    @ResponseBody
    public List<Map<String,Object>> querySales(){
        return userService.queryAllSales();
    }

    /**
     * 更新营销机会的开发状态
     * @param id
     * @param devResult
     * @return
     */
    @RequestMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id,Integer devResult){
        saleChanceService.updateSaleChanceDevResult(id,devResult);
        return success("开发状态更新成功！");
    }




}
