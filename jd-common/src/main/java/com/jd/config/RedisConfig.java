package com.jd.config;



import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import java.util.HashSet;
import java.util.Set;

@Configuration    //标识我是一个配置类
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
    @Value("${redis.nodes}")
    private String nodes;  //获取配置文件的节点信息（IP地址和端口号）
    //用来返回redis分片信息的链接对象
    @Bean
    public JedisCluster jedisCluster(){
        Set<HostAndPort> nodesSets = new HashSet<>();
        //按照逗号把配置的节点字符串分成字符串数组
        String[] strNodes = nodes.split(",");
        for (String strNode:strNodes){
            String[] node = strNode.split(":");
            //IP地址
            String host= node[0];
            //端口号
            int port = Integer.parseInt(node[1]);
            //把每个IP地址和端口号的对象装入集合
            nodesSets.add(new HostAndPort(host,port));
        }
        //返回redis集群连接对象
        return  new JedisCluster(nodesSets);
    }
}
