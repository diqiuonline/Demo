package com.itheima.shop;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/4/25 11:07
 */
@EnableDubboConfiguration
@SpringBootApplication
public class PayWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayWebApplication.class, args);
    }
}
