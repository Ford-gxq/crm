package com.xxxx.crm.aop;
import com.xxxx.crm.annotation.RequiredPermission;
import com.xxxx.crm.exceptions.NoAuthException;
import com.xxxx.crm.exceptions.NoLoginException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpSession;
import java.util.List;
/**
 *定义aop切面类 拦截指定注解标注的方法
 */
@Component
@Aspect
public class PermissionsProxy {
    @Autowired
    HttpSession httpSession;

    @Around(value="@annotation(com.xxxx.crm.annotation.RequiredPermission)")
    public Object testAop(ProceedingJoinPoint pjp) throws Throwable {
        //判断是否有权限
        List<String> permissions = ( List<String>)httpSession.getAttribute("permissions");
        //判断
        if(permissions== null || permissions.size()==0){
            throw new NoLoginException();
        }
        //有权限，获取权限码
        MethodSignature signature = (MethodSignature)pjp.getSignature();
        RequiredPermission re = signature.getMethod().getDeclaredAnnotation(RequiredPermission.class);
        //
        if(!(permissions.contains(re.code()))){
            throw  new NoAuthException();
        }
        Object result = pjp.proceed();
        return  result;
    }
}
