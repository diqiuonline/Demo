package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * User: 李锦卓
 * Time: 2019/5/18 21:20
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class redisTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Test
    public void testRedis() {
        //定义key
        String key = "user_token:5ea527ec-e0fe-4b34-9ba3-d0f7709f7a1c";
        //定义value
        Map<String,String> value = new HashMap<>();
        value.put("jwt","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6IlhjV2ViQXBwIiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOm51bGwsInV0eXBlIjpudWxsLCJpZCI6bnVsbCwiZXhwIjoxNTU4MjI5NzczLCJqdGkiOiI1ZWE1MjdlYy1lMGZlLTRiMzQtOWJhMy1kMGY3NzA5ZjdhMWMiLCJjbGllbnRfaWQiOiJYY1dlYkFwcCJ9.i4C3McfqO2t15dldiX0-ifP9rTwzLEC5GInibjpZmxa1NpyuUDj2bZW_XUSwGRN2jWNO_hAzMbYd7OWWoYVeZGJOO8JsF_cMn8BiUtBnd22cjOsUMVauiPrLbKFWNRtGhAXwNhGXQyNU-QI9ZHIIfVSen2mkt4rt_PMGpI0xO25RR3afo4kYGA-kaw8HO6pMu_dRqpU5aFvmEQVMxbUnNyofDk6GZfiViBazP6ayd7x4RWEUjMvl3RfXRL8U002V8ip1rP9rehRd2dlIzpO38eMPExu4PVf-5RxhZxUSQSnxaEq0vyaGfn8wFvSqeJVKADbIrW_CZeSnLQ7MJqnLXw");
        value.put("refresh_token","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6IlhjV2ViQXBwIiwic2NvcGUiOlsiYXBwIl0sImF0aSI6IjVlYTUyN2VjLWUwZmUtNGIzNC05YmEzLWQwZjc3MDlmN2ExYyIsIm5hbWUiOm51bGwsInV0eXBlIjpudWxsLCJpZCI6bnVsbCwiZXhwIjoxNTU4MjI5NzczLCJqdGkiOiJhOTY3MTZiOS0xOWY3LTRmY2MtYmRlNC01ODliZThiMTgzYzkiLCJjbGllbnRfaWQiOiJYY1dlYkFwcCJ9.E_UXwZWeWUQnRAeNV8WvF3li5jqP3jO_ZdGBttO34je1vQ6Urxg-3YseZ_LQJeTZEgBapmCmoJJwpdz-K2_pjthlU6geMjScEURWcciJn-BpWBXThERKkLXAzu6yli3RJIZ0WRPAHS1iWcEOwa-PTNiNa-BbcC9Cgv57JtuAabSEMSdDD-pHLrSy6-liQXlwJuyfxDyAcStHgDYFx-hWCYw56Pdqtuy7HXxGihvq-OpyypRwOIP5fXM3c2n5ouFobUPlyO4qc6gWDh60qgZxJl1GhojwajaiIY11Gq7hAFybAFp6EuP6uCPwW1drLx4wjaUsJeF4obMf_HLxlhU9zA");
        String jsonString = JSON.toJSONString(value);
        Boolean aBoolean = stringRedisTemplate.hasKey(key);
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        System.out.println(aBoolean + expire.toString());
        //存储数据
        //stringRedisTemplate.boundValueOps(key).set(jsonString,30,TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(key, jsonString, 30, TimeUnit.SECONDS);
        //读取数据
        String s = stringRedisTemplate.opsForValue().get(key);
        System.out.println(s);
    }
}