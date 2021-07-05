package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.mapper.CusDevPlanMapper;
import com.xxxx.crm.mapper.SaleChanceMapper;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.CusDevPlan;
import com.xxxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan, Integer> {

  @Autowired(required = false)
  private CusDevPlanMapper cusDevPlanMapper;
  @Autowired(required = false)
  private SaleChanceMapper saleChanceMapper;
    /**
     * 多条件计划项列表
     * @param cusDevPlanQuery
     * @return
     */
    public Map<String, Object> queryCusDevPlan(CusDevPlanQuery cusDevPlanQuery) {
        //实例化Map
        Map<String, Object> map = new HashMap<>();
        //开始分页
        PageHelper.startPage(cusDevPlanQuery.getPage(), cusDevPlanQuery.getLimit());
        PageInfo<CusDevPlan> pageInfo = new PageInfo(cusDevPlanMapper.selectByParams(cusDevPlanQuery));
        //存储
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        //返回map
        return map;
    }
    /**
 * 添加计划项
 *  1. 参数校验
 *      营销机会ID  非空  记录必须存在
 *      计划项内容   非空
 *      计划项时间   非空
 *  2. 设置参数默认值
 *      is_valid
 *      crateDate
 *      updateDate
 *  3. 执行添加，判断结果
 */
@Transactional(propagation = Propagation.REQUIRED)
public void saveCusDevPlan(CusDevPlan cusDevPlan){
// 1. 参数校验
//营销机会非空，计划项内容非空，计划项时间非空
 checkCusDevPlanParams(cusDevPlan.getId(),cusDevPlan.getPlanItem(),cusDevPlan.getCreateDate());


// 2. 设置参数默认值is_valid 、 crateDate、 updateDate
  cusDevPlan.setIsValid(1);//setIsValid有效状态，1为有效，0为无效
  cusDevPlan.setCreateDate(new Date());
  cusDevPlan.setUpdateDate(new Date());
// 3. 执行添加，判断结果
    AssertUtil.isTrue(insertSelective(cusDevPlan)<1,"客户开发数据项添加失败！");

}
    /**
     * 验证参数
     * @param saleChanceId-->销售机会id
     * @param planItem--->计划项
     * @param planDate--->计划日期
     */
    private void checkCusDevPlanParams(Integer saleChanceId, String planItem, Date planDate) {

    //若销售机会id为空或根据销售机会id查询的销售客户不存在，则表示数据记录不存在
    AssertUtil.isTrue(null ==saleChanceId || saleChanceMapper.selectByPrimaryKey(saleChanceId)==null,"数据记录不存在");
   //计划项内容非空
   AssertUtil.isTrue(StringUtils.isBlank(planItem),"请输入计划项目");
   //计划项日期非空
   AssertUtil.isTrue(null==planDate,"计划项时间不能为空");

    }
/**
 * 更新计划项
 *  1.参数校验
 *      id  非空 记录存在
 *      营销机会id 非空 记录必须存在
 *      计划项内容  非空
 *      计划项时间 非空
 *  2.参数默认值设置
 *      updateDate
 *  3.执行更新  判断结果
 */
public void updateCusDevPlan(CusDevPlan cusDevPlan){
//如果待开发客户的id为空或者根据开发客户的id查询的对象为空，则更新的数据不存在
    AssertUtil.isTrue(null==cusDevPlan.getId()|| null==selectByPrimaryKey(cusDevPlan.getId()),"待更新的数据不存在");
    // 1. 参数校验
    //计划开发的客户存在，计划项内容非空，计划项时间非空
  checkCusDevPlanParams(cusDevPlan.getId(),cusDevPlan.getPlanItem(),cusDevPlan.getPlanDate());
    // 2. 设置参数默认值
  cusDevPlan.setUpdateDate(new Date());
  // 3. 执行添加，判断结果
  AssertUtil.isTrue(updateByPrimaryKeySelective(cusDevPlan)<1,"更新销售客户计划数据项失败");

}
    /**
     * 删除计划项
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void delCusDevPlan(Integer id){
        CusDevPlan cusDevPlan =selectByPrimaryKey(id);
        AssertUtil.isTrue(null==id || null ==cusDevPlan ,"待删除记录不存在!");
        cusDevPlan.setIsValid(0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(cusDevPlan)<1,"计划项记录删除失败!");
    }


}
