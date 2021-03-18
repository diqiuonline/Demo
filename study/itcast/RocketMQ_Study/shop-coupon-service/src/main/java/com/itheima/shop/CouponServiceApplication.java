package com.itheima.shop;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/1/11 21:17
 */
@SpringBootApplication
@EnableDubboConfiguration
public class CouponServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CouponServiceApplication.class, args);
    }
}