package com.simple.common;

import com.simple.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Objects;

/**
 * Create by S I M P L E on 2018/03/09 13:18:28
 */
public class RedisPool {

    //Jedis连接池
    private static JedisPool pool;
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
    //redis IP
    private static String redisIp = PropertiesUtil.getProperty("redis.ip");
    //redis port
    private static Integer redisPort = Integer.parseInt(Objects.requireNonNull(PropertiesUtil.getProperty("redis.port")));

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
        pool = new JedisPool(config, redisIp, redisPort, 1000 * 2);
    }

    //执行初始化
    static {
        initPool();
    }

    //开放到外部供使用
    //从连接池中获取一个实例
    public static Jedis getJedis() {
        return pool.getResource();
    }

    //将实例放回连接池
    public static void returnResource(Jedis jedis) {
        pool.returnResource(jedis);
    }

    //将broken实例放回broken的连接池
    public static void returnBrokenResource(Jedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("simple123", "simple123");
        returnResource(jedis);
        pool.destroy();

    }

}
