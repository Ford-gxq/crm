package com.xxxx.crm.config;

import com.xxxx.crm.interceptors.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 这个类相当于SpringMvc.xml的配置文件
 */
@Configuration//这个注解表明这是一个参数配置类
public class MvcConfig implements WebMvcConfigurer {
    @Bean
    public NoLoginInterceptor noLoginInterceptor() {
        return new NoLoginInterceptor();
    }
   /**
    * 添加拦截器
    * @param registry
    */
public void addInterceptors(InterceptorRegistry registry){
    // 需要一个实现HandlerInterceptor接口的拦截器实例，这里使用的是 NoLoginInterceptor
   registry.addInterceptor(noLoginInterceptor()).
           //用于设置拦截器的过滤路径规则
           addPathPatterns("/**")
           //用于设置不需要拦截的过滤规则
           .excludePathPatterns("/index","/user/login","/css/**","/images/**","/js/**","/lib/**");


}


}
