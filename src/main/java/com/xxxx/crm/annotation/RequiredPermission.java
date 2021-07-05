package com.xxxx.crm.annotation;

import java.lang.annotation.*;

/**
 *Retention /rɪˈtenʃn/ n. 保留 --->来定义一个注解的保存范围。
 *  Documented：将自定义注解设置为文档说明信息。
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredPermission {
    String code() default "";
}