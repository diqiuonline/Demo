package com.dhcc.nacos.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/5 20:58
 */
@RestController
public class RestProviderController {
    // 暴漏restFul接口
    @GetMapping(value = "/service")
    public String service() {
        System.out.println("provider invoke");
        return "provider invoke";

    }
}
