package cn.itcast.bos.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

/**
 * User: 李锦卓
 * Time: 2018/8/29 18:58
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class RedisTemplateTest {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Test
    public void test1(){
        redisTemplate.opsForValue().set("citye", "北京", 30, TimeUnit.SECONDS);
        System.out.println(redisTemplate.opsForValue().get("citye"));
    }
}