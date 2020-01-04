package com.dhcc.web.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * User: 李锦卓
 * Time: 2019/2/20 15:22
 */
@SpringBootApplication
@RequestMapping("/teacher")
public class App {
    @RequestMapping("/list")
    public String getString(){
        return "list";
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}