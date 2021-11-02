package com.roncoo.eshop.inventory.dao.impl;

import com.roncoo.eshop.inventory.dao.RedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

@Repository
public class RedisDaoImpl implements RedisDao {
    @Autowired
    JedisCluster jedisCluster;
    @Override
    public void set(String key, String value) {
        jedisCluster.set(key, value);
    }

    @Override
    public String get(String key) {
        return jedisCluster.get(key);
    }
}
