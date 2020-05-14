package com.jd;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

public class testSentinel {
    //测试reedis哨兵机制
    @Test
    public void  testSentinel(){
        //需要用到jedis哨兵连接池对象；需要通过连接池对象获取jedis客户端对象
        //1.创建set集合用来存放哨兵的信息：ip地址和端口号
        Set<String> sentinels = new HashSet<>();
        sentinels.add("192.168.244.10:26379");
        //2.要创建哨兵连接池对象(JedisSentinelPool);  mymaster为哨兵配置文件中为主机指定的名称
        JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinels);
        //3.获取jedis客户端对象
        Jedis jedis = pool.getResource();
        //4.存取测试(set/get)
        jedis.set("1704","下午好！！！");
        System.out.println(jedis.get("1704"));

    }
}
