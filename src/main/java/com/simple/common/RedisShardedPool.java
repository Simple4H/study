package com.simple.common;

import com.simple.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Create by S I M P L E on 2018/03/13 21:56:35
 */
public class RedisShardedPool {
    //ShardedJedis连接池
    private static ShardedJedisPool pool;
    //最大连接数
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20"));
    //在jedis pool中，最大Idle空闲jedis实例的个数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));
    //在jedis pool中，最小Idle空闲jedis实例的个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "2"));
    //在borrow（取）一个jedis实例的时候，是否需要进行验证
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));
    //在return（返回）一个jedis实例的时候，是否需要进行验证
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.return", "false"));
    //redis1 IP
    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");
    //redis1 port
    private static Integer redis1Port = Integer.parseInt(Objects.requireNonNull(PropertiesUtil.getProperty("redis1.port")));
    //redis2 IP
    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");
    //redis2 port
    private static Integer redis2Port = Integer.parseInt(Objects.requireNonNull(PropertiesUtil.getProperty("redis2.port")));

    //初始化连接池
    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        //连接耗尽时候，是否阻塞，true阻塞到超时，false则是抛出异常
        config.setBlockWhenExhausted(true);
        JedisShardInfo info1 = new JedisShardInfo(redis1Ip, redis1Port, 1000 * 2);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip, redis2Port, 1000 * 2);
        List<JedisShardInfo> shardInfoList = new ArrayList<>(2);
        shardInfoList.add(info1);
        shardInfoList.add(info2);

        pool = new ShardedJedisPool(config, shardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    //执行初始化
    static {
        initPool();
    }

    //开放到外部供使用
    //从连接池中获取一个实例
    public static ShardedJedis getJedis() {
        return pool.getResource();
    }

    //将实例放回连接池
    public static void returnResource(ShardedJedis jedis) {
        pool.returnResource(jedis);
    }

    //将broken实例放回broken的连接池
    public static void returnBrokenResource(ShardedJedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();
        for (int i = 0; i < 1000; i++) {
            jedis.set("key" + i, "value" + i);
        }
        returnResource(jedis);
    }
}
