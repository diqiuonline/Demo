package com.dhcc.nacos.provider.controller;

import com.dhcc.microservice.service1.api.Service1Api;
import com.dhcc.microservice.service2.api.Service2Api;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

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
        return forObject;
    }

    //指定服务名
    String serviceId = "alibaba-nacos-restful-provider";
    //通过负载均衡发现地址 流程是从服务发现中心拿alibaba-nacos-restful-provider服务的列表 通过负载均衡算法获取一个地址
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping(value = "/service1")
    public String service1() {
        RestTemplate restTemplate = new RestTemplate();

        //发现一个地址
        ServiceInstance serviceInstance = loadBalancerClient.choose(serviceId);
        URI uri = serviceInstance.getUri();

        String forObject = restTemplate.getForObject(uri + "/service", String.class);
        System.out.println(forObject);
        return forObject;
    }


    @Reference
    private Service2Api service2Api;

    @GetMapping(value = "/service2")
    public String service2() {
        String s = service2Api.dubboService2();
        System.out.println(s);
        return s;
    }

    @Reference
    private Service1Api service1Api;

    @GetMapping(value = "/service3")
    public String service3() {
        String s = service1Api.dubboService1();
        System.out.println(s);
        return s;
    }

    @Value("${common.name}")
    private String common_name;
    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    @GetMapping(value = "/config")
    public String getValue() {
        //return common_name;
        return  configurableApplicationContext.getEnvironment().getProperty("common.name");
    }
}
