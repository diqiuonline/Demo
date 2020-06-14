package com.dhcc.shanjupay.merchant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/14 19:37
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RestTemplateTest {
    public static final Logger log = LoggerFactory.getLogger(RestTemplateTest.class);
    //测试rest客户端向验证服务器端发http请求
    @Autowired
    public RestTemplate restTemplate;

    @Test
    public void getHtml() {
        String url = "http://www.baidu.com/";
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);
        log.info(result.getBody());

    }
    @Test
    public void getMessage() {
        String url = "http://localhost:56085/sailing/generate?effectiveTime=600&name=sms";
        //请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        //请求体
        Map<String, Object> body = new HashMap<>();
        body.put("mobile", "43524366");
        //请求信息
        HttpEntity httpEntity = new HttpEntity(body,httpHeaders);

        ResponseEntity<Map> result = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
        Map result1 = (Map) result.getBody().get("result");
        String key = (String) result1.get("key");
        log.info(key);
    }

}
