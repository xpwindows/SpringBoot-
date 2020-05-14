package com.jd;

import org.junit.Test;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

import java.util.ArrayList;
import java.util.List;

public class testShards {
    @Test
    public void testShards(){
        //JedisShardInfo存放分片信息的对象
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        String host = "192.168.244.10";
        shards.add(new JedisShardInfo(host, 6379));
        shards.add(new JedisShardInfo(host, 6380));
        shards.add(new JedisShardInfo(host, 6381));
        //redis分片客户端
        ShardedJedis jedis = new ShardedJedis(shards);
        jedis.set("1704","sb");
        System.out.println("获取数据："+jedis.get("1704"));
    }

}
