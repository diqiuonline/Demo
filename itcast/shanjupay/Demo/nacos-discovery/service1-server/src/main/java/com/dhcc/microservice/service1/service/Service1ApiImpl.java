package com.dhcc.microservice.service1.service;

import com.dhcc.microservice.service1.api.Service1Api;
import com.dhcc.microservice.service2.api.Service2Api;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/6 21:51
 */
@Service
public class Service1ApiImpl implements Service1Api {
    @Reference
    private Service2Api service2Api;


    public String dubboService1() {

        return "service1 调用"+service2Api.dubboService2();
    }
}
