package com.itheima.shop;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.itheima.utils.IDWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/1/11 21:20
 */
@SpringBootApplication
@EnableDubboConfiguration
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    @Bean
    public IDWorker getBean() {
        return new IDWorker(1, 1);
    }
}
