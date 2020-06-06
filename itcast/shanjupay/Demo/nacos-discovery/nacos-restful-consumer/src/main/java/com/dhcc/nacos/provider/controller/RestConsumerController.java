package com.dhcc.nacos.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/5 21:07
 */
@RestController
public class RestConsumerController {
    //要进行远程，需要知道提供方的ip和端口
    @Value("${provider.address}")
    private String provider;

    @GetMapping(value = "/service")
    public String service() {
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject(provider, String.class);
        System.out.println(forObject);
        return forObject ;
    }
}
