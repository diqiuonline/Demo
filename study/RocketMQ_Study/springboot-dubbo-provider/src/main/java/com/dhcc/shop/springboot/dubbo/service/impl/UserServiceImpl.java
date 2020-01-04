package com.dhcc.shop.springboot.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dhcc.shop.service.IUseService;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = IUseService.class)
public class UserServiceImpl implements IUseService {
    @Override
    public String sayHello(String name) {

        return "Hello"+name;
    }
}
