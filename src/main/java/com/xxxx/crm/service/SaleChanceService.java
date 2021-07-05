package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.mapper.SaleChanceMapper;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance, Integer> {

    @Autowired(required = false)
    private SaleChanceMapper saleChanceMapper;
    /**
     * 多条件分页查询营销机会 (BaseService 中有对应的方法)
     前端数据表渲染需要键值对类型的json数据格式，因此多条件查询的结果类型应该为一个装键值对的集合。
     * @param query-->查询参数为销售机会查询对象
     * @return
     */

    public Map<String, Object> querySaleChanceByParams (SaleChanceQuery query) {
        //创建一个map集合用于存放返回的数据格式的参数和数据
        Map<String, Object> map = new HashMap<>();
        //分页初始化，getPage当前的页码，getLimit-->每页显示的条数
        PageHelper.startPage(query.getPage(), query.getLimit());
        //从数据库中依据查询参数对象获取销售机会的集合list
        List<SaleChance>  list= saleChanceMapper.selectByParams(query);
        //创建pageInfo对象，分页信息对象，再将查询所得的销售机会集合注入到分页信息对象中
        PageInfo<SaleChance>  pageInfo = new PageInfo<>(list);
        //将查询所得的数据和参数存入map集合
        map.put("code",0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());//总记录数
        map.put("data", pageInfo.getList());//销售机会数据集合
        return map;
    }
/**
 * 营销机会数据添加
 *   1.参数校验
 *      customerName:非空
 *      linkMan:非空
 *      linkPhone:非空 11位手机号
 *   2.设置相关参数默认值
 *      state:默认未分配  如果选择分配人  state 为已分配
 *      assignTime:如果  如果选择分配人   时间为当前系统时间
 *      devResult:默认未开发 如果选择分配人devResult为开发中 0-未开发 1-开发中 2-开发成功 3-开发失败
 *      isValid:默认有效数据(1-有效  0-无效)
 *      createDate updateDate:默认当前系统时间
 *   3.执行添加 判断结果
 */
    /**
     * 营销机会数据添加
     * @param saleChance
     */
  @Transactional(propagation = Propagation.REQUIRED)
  public void  saveSaleChance(SaleChance saleChance){
   // 1.参数校验-->customerName:非空/linkMan:非空/linkPhone非空
      checkParmas(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
   //2. 未分配的操作-->如果未分配saleChance.getAssignMan()的值为空
      //已分配的操作
   if(StringUtils.isBlank(saleChance.getAssignMan())){
       //未分配
       saleChance.setState(0);//0--未分配，1，已经分配
       //开发状态 未开发
       saleChance.setDevResult(0);
       //分配时间
       saleChance.setAssignTime(null);
   }if (StringUtils.isNotBlank(saleChance.getAssignMan())){//销售机会的指派人不为空表示已经分配
          //已经分配
          saleChance.setState(1);
          //开发中
          saleChance.setDevResult(1);//0-未开发，1--开发中，2，成功，3--失败
          //分配时间
          saleChance.setAssignTime(new Date());
      }
    //系统默认时间
      saleChance.setCreateDate(new Date());
      saleChance.setUpdateDate(new Date());
    // 3.执行添加 判断结果
      AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)<1,"添加失败");
  }
    /**
     * 客户数据添加操作的-->参数的验证
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkParmas(String customerName, String linkMan, String linkPhone) {
    //客户名非空判断
    AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名不能为空");
    //联系人非空判断
    AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空");
    //电话不能为空
    AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系电话不能为空");
    //请输入合法的手机号
    AssertUtil.isTrue(!(PhoneUtil.isMobile(linkPhone)),"你输入的手机号码不合法");

    }
/**
 * 营销机会数据更新
 *  1.参数校验
 *      id:记录必须存在
 *      customerName:非空
 *      linkMan:非空
 *      linkPhone:非空，11位手机号
 *  2. 设置相关参数值
 *      updateDate:系统当前时间
 *         原始记录 未分配 修改后改为已分配(由分配人决定)
 *            state 0->1
 *            assginTime 系统当前时间
 *            devResult 0-->1
 *         原始记录  已分配  修改后 为未分配
 *            state  1-->0
 *            assignTime  待定  null
 *            devResult 1-->0
 *  3.执行更新 判断结果
 */
//营销机会数据更新
@Transactional(propagation = Propagation.REQUIRED)
 public void updateSaleChance(SaleChance saleChance){
    // 1.参数校验
    // 通过id查询记录
    SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
    // 判断是否为空
    AssertUtil.isTrue(null==temp,"待更新记录不存在");
    // 表单填写的参数校验
    checkParmas(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
    // 2. 设置相关参数值
    saleChance.setUpdateDate(new Date());
    /*如果从数据库中查询到的营销客户的指派人为空，且当前要修改的营销客户的数据对象也没有分配则可进行指派操作
    * */
    if(StringUtils.isBlank(temp.getAssignMan())&& StringUtils.isNotBlank(saleChance.getAssignMan())){
        // 如果原始记录未分配，修改后改为已分配
      saleChance.setState(1);
      saleChance.setAssignTime(new Date());
       saleChance.setDevResult(1);//开发状态
    }else if (StringUtils.isNotBlank(temp.getAssignMan())&& StringUtils.isBlank(saleChance.getAssignMan())){
   /*如果从数据库中查询到的营销客户的指派人不为空，表示已经有人在开发该客户；
      且当前要修改的营销客户的数据对象没有指派人,则不能再重复指派   */
        //已经分配的，维持原来分配状态
        saleChance.setState(0);
        //开发状态
        saleChance.setDevResult(0);
        //修改 分配时间
        saleChance.setAssignTime(null);
        //分配人
        saleChance.setAssignMan("");
    }
    saleChance.setUpdateDate(new Date());
    // 3.执行更新 判断结果
    AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)<1,"更新失败");
 }
    /**
     * 批量删除
     * @param array
     */
    public void removeSaleChance(Integer []array){
        AssertUtil.isTrue(array == null || array.length==0,"请选择要删除的数据");
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(array)<1,"批量删除失败了");
    }
    /**
     * 更新营销机会的状态
     *      成功 = 2
     *      失败 = 3
     * @param id
     * @param devResult
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChanceDevResult(Integer id, Integer devResult) {
        AssertUtil.isTrue( null ==id,"待更新记录不存在!");
        SaleChance temp =selectByPrimaryKey(id);
        AssertUtil.isTrue( null ==temp,"待更新记录不存在!");
        temp.setDevResult(devResult);
        AssertUtil.isTrue(updateByPrimaryKeySelective(temp)<1,"机会数据更新失败!");
    }




}
