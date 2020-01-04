package com.dhcc.web;

import com.sun.xml.internal.rngom.parse.host.Base;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * User: 李锦卓
 * Time: 2019/2/27 10:42
 */
@SpringBootTest//(classes = Base.class)
@RunWith(SpringRunner.class)
public class test1 {
    static {
        System.out.print("pong");
    }
    @Test
    public void demo(){
        Thread t = new Thread() {
            public void run() {
                pong();
            }
        };
        t.start();
        System.out.print("ping");
    }
    static void pong() {
        System.out.print("pong");
    }
}