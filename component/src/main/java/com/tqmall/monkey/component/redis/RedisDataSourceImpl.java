package com.tqmall.monkey.component.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by zxg on 15/7/20.
 */
@Component
@Slf4j
public class RedisDataSourceImpl implements RedisDataSource {


    @Autowired
    private ShardedJedisPool shardedJedisPool;

    public ShardedJedis getRedisClient() {
        try {

            ShardedJedis shardJedis = shardedJedisPool.getResource();
            return shardJedis;

        } catch (Exception e) {
             log.error("getRedisClent error", e);
        }
        return null;
    }

    @Override
    public void returnResource(ShardedJedis shardedJedis, boolean broken) {
        try {
            shardedJedis.close();
        } catch (BeansException e) {
            log.error("BeansException destroy", e);
        }
    }
}
