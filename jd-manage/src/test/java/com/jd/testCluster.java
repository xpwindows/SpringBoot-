package com.jd;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

public class testCluster {
@Test
    public void testCluster(){
        // 1.创建set集合用来装HostAndPort节点对象
        Set<HostAndPort> nodes = new HashSet<>();
        // 2.往集合添加节点对象HostAndPort
        nodes.add(new HostAndPort("192.168.244.10",7000));
        nodes.add(new HostAndPort("192.168.244.10",7001));
        nodes.add(new HostAndPort("192.168.244.10",7002));
        nodes.add(new HostAndPort("192.168.244.10",7003));
        nodes.add(new HostAndPort("192.168.244.10",7004));
        nodes.add(new HostAndPort("192.168.244.10",7005));
        //创建集群客服端对象
        JedisCluster jedisCluster  = new JedisCluster((nodes));
        // 3.set数据，取出数据测试
        jedisCluster.set("1760","集群搭建完毕");
        System.out.println(jedisCluster.get("1760"));
    }
}
