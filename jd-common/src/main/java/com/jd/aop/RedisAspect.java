package com.jd.aop;

import com.jd.anno.Cache;
import com.jd.enu.KEY_ENUM;
import com.jd.util.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisCluster;

/**
 * redis缓存查询的切面类
 */
@Slf4j   //日志注解
@Component   //将对象的创建交给spring管理
@Aspect    //表示这是一个切面    切面=切入点表达式+通知
public class RedisAspect {
    @Autowired(required = false)  //自动注入redis集群客户端对象
    private JedisCluster jedisCluster;
    /**
     * 环绕通知：需要用ProceedingJoinPoint连接点对象
     */
    @Around(value="@annotation(cache)")  //切面点表达式，就表示加了Cache注解的方法上执行这个环绕通知方法
    public Object around(ProceedingJoinPoint joinPoint, Cache cache){
        //1.动态获取key;  去调用下面写的getKey方法获取
       String key= getKey(joinPoint,cache);
        //2.通过key查询redis获取数据
        String resultJSON = jedisCluster.get(key);
        Object resultData=null;
        try {
            //3.判断是否有数据
            if (StringUtils.isEmpty(resultJSON)){
                //3.1如果缓存中没有数据；执行目标方法查询数据库
               resultData = joinPoint.proceed();//执行目标方法
                //3.2将数据转换成json串，判断是否指定有数据失效时间
                String json = ObjectMapperUtil.toJSON(resultData);
                //3.3 把数据存入redis缓存
                if (cache.secondes()==0){ //永不失效
                    jedisCluster.set(key,json);
                }else {
                    jedisCluster.setex(key,cache.secondes(),json);
                }
                System.out.println("AOP查询mysql数据库成功!!!");
            }else {
                //4.如果缓存中有数据  ；去调用下面写的getTargetClass方法获取返回值类型returnType
               Class returnType= getTargetClass(joinPoint);
               //把json串转换成目标方法的返回值类型
                resultData= ObjectMapperUtil.toObject(resultJSON, returnType);
                System.out.println("AOP查询缓存！！！");
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            //日志打印
            log.error(throwable.getMessage());
            //抛运行时异常
            throw new RuntimeException(throwable);
        }
        //返回对象
        return resultData;
    }
    /**
     *用来获取目标方法的返回值类型
     */
    private Class getTargetClass(ProceedingJoinPoint joinPoint) {
        //获取到了目标方法的前面对象
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取方法的返回值类型,返回
        return signature.getReturnType();
    }
    /**
     *动态获取key的方法;
     * key的定义规则：
     * 1.如果用户使用AUTO，则自动生成key 方法名：：第一个参数
     * 2.如果用户使用EMPTY,使用用户自己指定的key;
     */
    private String getKey(ProceedingJoinPoint joinPoint, Cache cache) {
        //1.判断用户选择的类型，如果选择EMPTY类型表示用指定的key
        if (KEY_ENUM.EMPTY.equals(cache.keyType())){
            System.out.println("用户指定的key="+cache.key());
            return cache.key();    //返回key
        }
        //2.如果是AUTO，表示用户用动态生成的key  方法名：：第一个参数
        //通过链接点对象获取方法签名 从而获取方法名称 ；链接对象也可以获取方法参数
        String methodName = joinPoint.getSignature().getName();
        //获取目标方法的第一个参数；把参数对象转换成字符串；
        String arg0 = String.valueOf(joinPoint.getArgs()[0]);
        System.out.println("拼接的key="+methodName+"::"+arg0);
        //拼接key 返回
        return methodName+"::"+arg0;   //返回key
    }
}
