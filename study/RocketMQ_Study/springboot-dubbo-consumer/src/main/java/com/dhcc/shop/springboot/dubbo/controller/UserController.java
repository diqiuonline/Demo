package com.dhcc.shop.springboot.dubbo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dhcc.shop.service.IUseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private IUseService useService;
    @RequestMapping("/sayhello")
    public String sayHello(String name){
        return useService.sayHello(name);
    }

}
