package com.jd.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jd.mapper.UserMapper;
import com.jd.pojo.User;
import com.jd.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.JedisCluster;

import java.util.Date;

/**
 * 服务的提供者；实现中立接口
 */
@Service       //阿里巴巴下的的注解
public class DubboUserServiceImpl implements DubboUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired    //注入redis集群客户端对象
    private JedisCluster jedisCluster;
    //把jt-web提交过来的user对象完善后保存到数据表中（tb_user）
    @Override
    public void saveUser(User user) {
        //1.对密码加密处理，MD5算法来加密，MD5算法是一种哈希算法，生成32位的16进制字符串
        String md5Pass =
                DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        //2.完善封装用户信息,   邮箱 创建时间  修改时间
        user.setEmail(user.getPhone())
                .setPassword(md5Pass)
                .setCreated(new Date())
                .setUpdated(user.getCreated());
        //3.调用mapper层的接口方法，插入数据
        userMapper.insert(user);
    }

    /** 单点登录
     *1.校验用户信息： 密码需要加密后查询数据库
     * 2.校验数据
     * 3.将数据保存到redis中
     *
     */
    @Override
    public String doLogin(User user) {
        //1.由于原来存入表中的密码是md5加密处理过的，所以查询的时候也要先对密码加密
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        //查询数据库；将对象中不为null的属性作为查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>(user);
        User userDB = userMapper.selectOne(queryWrapper);
        //System.out.println("userDB="+userDB.getUsername()+";"+userDB.getPassword());
        //2.判断查询结果是否为null
        String token=null;
        //查询数据不为null的情况；实际就是用户名与密码正确的情况
        if (userDB!=null){
            //2.2如果不为null；把数据存入redis；需要key 和value;
            //需要保证key唯一性；可以用当前毫秒值和用户名来生成 key
            String tokenTemp = "JT_TICKET_" + System.currentTimeMillis() + user.getUsername();
            //对tokenTemp加密
           tokenTemp= DigestUtils.md5DigestAsHex(tokenTemp.getBytes());
           //生成value数据；userDB  转换成json字符串；
            //为了安全，需要对数据进行脱敏处理；重设密码；
            userDB.setPassword("123456你猜对吗？？");
            //把对象转换成json字符串; 这个就是需要存入redis的value
            String userJSON = ObjectMapperUtil.toJSON(userDB);
            //存入redis，同时设置存活时间为7天
            jedisCluster.setex(tokenTemp,7*24*3600,userJSON);
            token=tokenTemp;
        }
        return token;
    }
}
