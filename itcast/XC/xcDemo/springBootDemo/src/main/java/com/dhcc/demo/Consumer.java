package com.dhcc.demo;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * User: 李锦卓
 * Time: 2018/12/17 11:23
 */
@Component
public class Consumer {
    @JmsListener(destination = "dhcc")
    public void readMessage(String s){
        System.out.println("接收到消息" + s);
    }
}