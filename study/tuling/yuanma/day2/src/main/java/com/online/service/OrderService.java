package com.online.service;

import com.spring.Component;
import com.spring.Scope;

/**
 * @Author 李锦卓
 * @Description TODO
 * @Date 2022/8/10 11:14
 * @Version 1.0
 */
@Component
@Scope("prototype")
public class OrderService {
    public void test() {
        System.out.println("-------");
    }
}
