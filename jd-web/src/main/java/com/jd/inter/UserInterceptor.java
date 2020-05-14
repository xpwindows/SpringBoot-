package com.jd.inter;

import com.alibaba.druid.util.StringUtils;
import com.jd.pojo.User;
import com.jd.util.ObjectMapperUtil;
import com.jd.util.UserThreadLocal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 拦截器
 */
@Component
public class UserInterceptor implements HandlerInterceptor{

    @Autowired
    private JedisCluster jediscluster;
    /**
     * boolean:
     * 		true: 表示放行
     * 		false:表示拦截  一般配合重定向使用
     *
     * 业务实现步骤
     * 	1.获取用户Cookie中的token信息.
     * 	2.校验数据是否有效
     * 	3.校验redis中是否有数据
     * 	如果上述操作正确无误.返回true
     * 	否则return false 重定向到登录页面
     *
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //1.获取cookie数据
        Cookie[] cookies = request.getCookies();
        String token = null;
        if(cookies.length>0) {
            for (Cookie cookie : cookies) {
                if("JT_TICKET".equals(cookie.getName())) {
                    //获取指定数据的值
                    token = cookie.getValue();
                    break;
                }
            }
        }
        //2.校验token的有效性
        if(!StringUtils.isEmpty(token)) {
            //2.1校验redis中是否有数据
            String userJSON = jediscluster.get(token);
            if(!StringUtils.isEmpty(userJSON)) {
                User user = ObjectMapperUtil.toObject(userJSON, User.class);
                //将user数据通过request对象封装;数据可以在相互转发的页面间共享
                request.setAttribute("JT_USER",user);
                //利用threadLocal实现数据共享
                UserThreadLocal.set(user);
                return true;
            }
        }
        //重定向到登录页面；必须重定向
        response.sendRedirect("/user/login.html");
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        UserThreadLocal.remove();
    }
}
