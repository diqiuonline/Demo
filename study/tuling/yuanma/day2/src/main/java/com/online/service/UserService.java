package com.online.service;

import com.spring.Autowired;
import com.spring.BeanNameAware;
import com.spring.Component;

/**
 * @Author 李锦卓
 * @Description TODO
 * @Date 2022/8/10 11:14
 * @Version 1.0
 */
@Component
public class UserService implements BeanNameAware,UserInterface {
    @Autowired
    OrderService orderService;
    private String beanName;
    @OnlineValue("diqiuonline")
    private String test;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    public void test() {
        System.out.println(test);
    }
}
