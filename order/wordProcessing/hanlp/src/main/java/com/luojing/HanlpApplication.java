package com.luojing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * User: 李锦卓
 * Time: 2019/8/23 16:35
 */
@SpringBootApplication
public class HanlpApplication {
    public static void main(String[] args) {
        SpringApplication.run(HanlpApplication.class, args);
        /*try {
            //Runtime.getRuntime().exec("cmd   /c   start   http://127.0.0.1:31200/docs.html");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}