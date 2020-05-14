package com.jd.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jd.pojo.User;
import com.jd.service.DubboUserService;
import com.jd.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {
    /**
     * 选择dubbo下的注解，check=false当服务提供方还没启动的时候，可以注入代理对象，
     * 这样就会避免出现空指针异常
     */
    @Reference(timeout = 3000,check =false)
    private DubboUserService userService;
    @Autowired
    private JedisCluster jedisCluster;
    /**
     * 通用跳转方法:
     * http://www.jt.com/user/login.html
     * http://www.jt.com/user/register.html
     */
    @RequestMapping("/{moduleName}")
    public String moduleName(@PathVariable String moduleName){
        return  moduleName;
    }
/**
 * 用户通过前端页面点击 立即注册 按钮之后 把用户信息保存到数据库表中；
 * 通过dubbo的远程过程调用来实现；去调用DubboUserService下的方法
 * 前端传来的参数：用户名  密码  手机号   ，用User接收
 * url：  user/doRegister
 * 要返回的数据： SysResult对象的json串
 */
@RequestMapping("/doRegister")
@ResponseBody
public SysResult saveUser(User user){
    //利用dubbo  rpc协议来完成远程过程调用
    userService.saveUser(user);
    return SysResult.success();
}
/**单点登录
 * url:user/doLogin
 * 需要接收的参数：username  password  可以通过封装类User接收
 * 需要返回给前端的数据：json数据：  可以用通用的SysReslut 调用成功或失败的方法返回；
 *1.关于Cookie生命周期问题：
 * 超过一定时间，cookie失效，需要重新登录；
 * cookie.setMaxAge（>0）;括号中的数字就是存活的时间；
 * cookie.setMaxAge（0）;表示删除cookie；
 * cookie.setMaxAge（-1）；会话关闭，同时删除cookie;
 *
 * 2.Cookie使用权限问题：
 * www.baidu.com
 * cookie.setPath(“/”);
 * www.baidu.com/aa/1.html  可以访问
 * cookie.setPath(“/bb”);
 * www.baidu.com/aa/1.html   不可以访问
 *
 * 3.设置cookie共享：每个网址都有自己固定的cookie信息，默认不共享；
 * 需要：
 * www.jt.com  一级域名
 * sso.jt.com  二级域名
 * item.jt.com 二级域名
 * 要求在一级域名与二级域名之间实现cookie共享；
 * 实现步骤：通过domain实现cookie共享
 */
@RequestMapping("/doLogin")
@ResponseBody
public SysResult doLogin(User user, HttpServletResponse response){
    //1.调用DubboUserService接口方法，把user对象带过去，需要返回一个TOKEN秘钥
    String token=userService.doLogin(user);
    //2.校验返回的token数据是否为null
    //2.1 如果为null，调用SysResult 失败的方法
    if (StringUtils.isEmpty(token)){
      return   SysResult.fail();
    }
    //2.2 如果不为null;把token写入Cookie中；cookie名字需要与前端页面jt-js获取cookie的变量名称相同；否则无法发起ajax请求
    Cookie cookie = new Cookie("JT_TICKET", token);
    cookie.setMaxAge(7*24*3600); //设置cookie最大存活时间7天
    //设置权限,根域名下共享
     cookie.setPath("/");
     //设置一级域名与二级域名cookie共享
    cookie.setDomain("jd.com");
    //把cookie装入response对象中
    response.addCookie(cookie);
    return  SysResult.success();
}
/**登出操作
 * url：/user/logout
 * 需要获取request对象中的cookie信息；
 * 需要把空的和存活时间为0的cookie添加到response对象中；
 * 删除redis中的token；
 * 最后重定向到首页；
 */
@RequestMapping("/logout")
public String logout(HttpServletRequest request,HttpServletResponse response){
    //1.删除redis中的token数据，清除浏览器cookie数据
    //1.1 从request对象中获取所有cookie信息;
    Cookie[] cookies = request.getCookies();
    //1.2遍历cookie信息，直到找到对应名称(JT_TICKET)的cookie为止
    String token=null;
    if (cookies.length>0){
        for (Cookie cookie:cookies){
            if ("JT_TICKET".equals(cookie.getName())){
                //获取对应的cookie名称的值
                token = cookie.getValue();
                break; //找到了跳出循环
            }
        }
    }
    //1.3删除redis中的token；要求token不为null再做处理
    if (!StringUtils.isEmpty(token)){
        //删除redis中token
        jedisCluster.del(token);
        //cookie不能被删除；把JT_TICKET名称的cookie值设置为空字符串；存活时间设置为0
        Cookie cookie = new Cookie("JT_TICKET", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setDomain("jd.com");
        response.addCookie(cookie);
    }
  return "redirect:/";
}
}
