package com.jd.Aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

@Aspect   //声明切面类
@Service   //交给spring管理
public class LogAspect {
    /**
     * 环绕通知
     */
    //环绕通知，itemServiceImpl 切入点
    @Around("bean(*ServiceImpl)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("方法开始执行！！！！");
        Object result = joinPoint.proceed();//执行拦截到的方法
        //System.out.println("result******="+result);
        System.out.println("方法执行结束！！！！");
        return result;
    }
}
