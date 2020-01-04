package com.dhcc.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: 李锦卓
 * Time: 2018/12/17 10:54
 */
@RestController
public class HelloWorldController {
    @Autowired
    private Environment environment;
    @RequestMapping("/info")
    public String info(){
        return "Helloworld"+environment.getProperty("url");
    }
}