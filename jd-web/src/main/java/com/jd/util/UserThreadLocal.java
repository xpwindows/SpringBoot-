package com.jd.util;

import com.jd.pojo.User;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

public class UserThreadLocal {
        //声明User类型的ThreadLocal
    private  static  ThreadLocal<User> threadLocal = new
                ThreadLocal<>();
    public static void set(User user){ //设置线程变量名
        threadLocal.set(user);


    }
    public static User get(){ //获取用户变量名
        return threadLocal.get();

    }
    //防止内存泄漏remove
    public static void remove(){
        threadLocal.remove();
    }

}
