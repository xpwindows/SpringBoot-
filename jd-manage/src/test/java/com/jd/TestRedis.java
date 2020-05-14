package com.jd;

import org.junit.Test;
import redis.clients.jedis.Jedis;

public class TestRedis {
    /**
     * Spring整合redis入门案例
     * 通过项目存取数据
     */
    @Test
    public void testRedis1(){
        //1.创建jedis对象，用来连接redis
        String host="192.168.244.10";  //redis所在服务器的ip地址
        int port=6379;   //redis端口号
        Jedis jedis = new Jedis(host, port);
        //2.存入数据s
        jedis.set("1704","good!!!");
        //3.取出数据，打印
        System.out.println(jedis.get("1704"));
    }

}
