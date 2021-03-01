package com.dhcc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MQSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(MQSpringBootApplication.class, args);
        log.info("消费者启动成功");
    }
}
