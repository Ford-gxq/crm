package com.xxxx.crm.controller;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.service.CusDevPlanService;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.vo.CusDevPlan;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


@Controller
@RequestMapping("cus_dev_plan")
public class CusDevPlanController  extends BaseController {

    @Autowired
    private CusDevPlanService cusDevPlanService;
    @Autowired
    private SaleChanceService saleChanceService;
    /**
     * 客户开发主页面
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "cusDevPlan/cus_dev_plan";
    }
 /**
 * 进入开发计划项数据页面
 * @param model
 * @param sid
 * @return
 */
 @RequestMapping("toCusDevPlanDataPage")
 public String toCusDevPlanDataPage(Model model,Integer sid){
   // 通过id查询营销机会数据
     SaleChance saleChance = saleChanceService.selectByPrimaryKey(sid);
     // 将数据存到作用域中
    model.addAttribute("saleChance",saleChance);
     return "cusDevPlan/cus_dev_plan_data";
 }
    /**
     * 查询营销机会的计划项数据列表
     * @param query
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> sayList(CusDevPlanQuery query){

        return cusDevPlanService.queryCusDevPlan(query);
    }
/**
 * 添加计划项
 * @param cusDevPlan
 * @return
 */
@RequestMapping("save")
@ResponseBody
public ResultInfo saveCusDevPlan(CusDevPlan cusDevPlan){
    cusDevPlanService.saveCusDevPlan(cusDevPlan);
    return success("客户开发计划项添加成功");

}
/**
     *客户开发计划，更新计划项
     * @param cusDevPlan
     * @return
     */
@RequestMapping("update")
@ResponseBody
public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan){
    cusDevPlanService.updateCusDevPlan(cusDevPlan);
    return success("计划项更新成功!");
}
/**
 * 转发跳转到计划数据项页面
 * @param sid
 * @param id
 * @param model
 * @return
 */
@RequestMapping("addOrUpdateCusDevPlanPage")
public String addOrUpdateCusDevPlanPage(Integer sid,Integer id, Model model){
    model.addAttribute("cusDevPlan",cusDevPlanService.selectByPrimaryKey(id));
    model.addAttribute("sid",sid);
    return "cusDevPlan/add_update";
}
    /**
     * 删除计划项
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(Integer id){
        cusDevPlanService.delCusDevPlan(id);
        return success("计划项删除成功!");
    }

}
