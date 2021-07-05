package com.xxxx.crm.exceptions;

import com.alibaba.fastjson.JSON;
import com.xxxx.crm.base.ResultInfo;
import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常统一处理
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    /**
     * 方法返回值类型
     *    视图
     *    JSON
     * 如何判断方法的返回类型：
     *    如果方法级别配置了 @ResponseBody 注解，表示方法返回的是JSON；
     *	  反之，返回的是视图页面
     * @param request
     * @param response
     * @param handler
     * @param e
     * @return
     */

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        /**
         * 判断异常类型
         *     如果是未登录异常，则先执行相关的拦截操作
         */
        //未登录异常的处理
        if(e instanceof NoLoginException){
            // 如果捕获的是未登录异常，则重定向到登录页面
            ModelAndView mv = new ModelAndView("redirect:/index");
            return mv;
        }
        //实例化对象
        ModelAndView mav=new ModelAndView("error");

        //设置默认异常处理
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("code",400);
        modelAndView.addObject("msg","系统异常请稍后再试...");

        /**
         * handler方法返回值有一下情况：
         *  JSON---resultInfo--->如果方法级别配置了 @ResponseBody 注解，表示方法返回的是JSON；
         *  String---view名--->不带@ResponseBody注解的返回的是视图
         *
         */

        // 判断 HandlerMethod
     if (handler instanceof HandlerMethod){
         // 类型转换
         HandlerMethod handlerMethod =(HandlerMethod)handler;
         // 获取方法上的 ResponseBody 注解
         ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
         // 判断 ResponseBody 注解是否存在 (如果不存在，表示返回的是视图;如果存在，表示返回的是JSON)
        if(null==responseBody){
            /**
             * 方法返回视图
             */
            if (e instanceof ParamsException) {
                ParamsException pe = (ParamsException) e;
                modelAndView.addObject("code", pe.getCode());
                modelAndView.addObject("msg", pe.getMsg());
            }
            return modelAndView;

        }else{
            /**
             * 方法上返回JSON--->ResultInfo  text/html;
             */
            ResultInfo  resultInfo = new ResultInfo();
            resultInfo.setCode(300);
            resultInfo.setMsg("系统异常，请稍微再试...");
        // 如果捕获的是自定义异常
            if (e instanceof ParamsException) {
                ParamsException pe = (ParamsException) e;
                resultInfo.setCode(pe.getCode());
                resultInfo.setMsg(pe.getMsg());
            }
        // 设置响应类型和编码格式 （响应JSON格式）
            response.setContentType("application/json;charset=utf-8");
        // 得到输出流
            PrintWriter out = null;
        try{
          out = response.getWriter();
            // 将对象转换成JSON格式，通过输出流输出,响应给请求的前台
            out.write(JSON.toJSONString(resultInfo));
            out.flush();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }finally {
            //关闭输出流
            if (out != null) {
                out.close();
            }
        }
            return null;
        }
     }

        return modelAndView;

    }
}
