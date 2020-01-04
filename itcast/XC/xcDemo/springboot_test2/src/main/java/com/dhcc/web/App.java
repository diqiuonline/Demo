package com.dhcc.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * User2: 李锦卓
 * Time: 2019/2/18 21:57
 */
/*@EnableAutoConfiguration
@ComponentScan("com.dhcc.web")*/
@SpringBootApplication
@MapperScan("com.dhcc.web")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}