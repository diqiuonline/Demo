package com.dhcc.shanjupay.transaction.config;

import com.dhcc.shanjupay.common.cache.Cache;
import com.dhcc.shanjupay.transaction.common.util.RedisCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/7/12 0:16
 */
@Configuration
public class RedisConfig {
    @Bean
    public Cache cache(StringRedisTemplate stringRedisTemplate) {
        return new RedisCache(stringRedisTemplate);
    }
}
