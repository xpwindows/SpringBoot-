package com.jd.Aspect;

import com.jd.vo.SysResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * 异常处理切面类
 */
//把切面切入到controller层，只要有指定异常抛出，就执行该类，并且向前端你返回json格式数据
@RestControllerAdvice
@Slf4j  //日主注解，使用该注解可以调用log.info() 或log.error()打印
//log.error()打印
public class SysResultAspect {
    /**
     * 如果程序报错，则统一返回异常信息
     * SysResult.fail()
     */
    //拦截异常的一个注解，如果遇到指定的异常 执行以下方法
    @ExceptionHandler({RuntimeException.class})
    public SysResult sysResultFail(Exception e) {
        e.printStackTrace();
        //控制台打印异常信息
        log.error("服务器异常信息：" + e.getMessage());
        return SysResult.fail();
    }
}

