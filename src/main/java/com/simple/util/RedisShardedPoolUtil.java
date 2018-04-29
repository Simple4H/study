package com.simple.util;

import com.simple.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;

/**
 * Create by S I M P L E on 2018/03/13 22:34:26
 */
@Slf4j
public class RedisShardedPoolUtil {

    //将一个key设置有效时间
    public static Long expire(String key, int exTime) {

        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("set key:{}  error", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    //创建不存在一个key，如果已经存在则跳过，并且设置其有效时间
    public static String setEx(String key, int exTime, String value) {

        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("exset key:{} value:{} error", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    //设置一个key，（会覆盖key相同的key）
    public static String set(String key, String value) {

        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    //获取一个key的value
    public static String get(String key) {

        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("set key:{} error", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    //删除一个key
    public static Long del(String key) {

        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static void main(String[] args) {
        ShardedJedis jedis = RedisShardedPool.getJedis();
        RedisPoolUtil.set("a","a");
        String value = RedisPoolUtil.get("a");
        RedisPoolUtil.setEx("b",60*10,"b");
        RedisPoolUtil.expire("a",20*10);
        RedisPoolUtil.del("a");
    }
}
