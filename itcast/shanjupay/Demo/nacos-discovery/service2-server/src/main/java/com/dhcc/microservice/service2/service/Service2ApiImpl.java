package com.dhcc.microservice.service2.service;

import com.dhcc.microservice.service2.api.Service2Api;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/6 21:51
 */
@Service
public class Service2ApiImpl implements Service2Api {
    public String dubboService2() {
        System.out.println("dubbvoService2");
        return "dubbvoService2";
    }
}
