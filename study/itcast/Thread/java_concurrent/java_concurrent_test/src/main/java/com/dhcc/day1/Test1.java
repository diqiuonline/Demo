package com.dhcc.day1;

import lombok.extern.slf4j.Slf4j;

/**
 * 李锦卓
 * 2022/3/21 22:07
 * 1.0
 */
@Slf4j(topic = "c.Test1")
public class Test1 {
    public static void main(String[] args) {
        Thread t = new Thread(){
            @Override
            public void run() {
                log.debug("running");
            }
        };
        t.setName("t1");
        t.start();
        log.debug("running");
    }
}
/**
 * 22:11:19 [main] c.Test1 - running
 * 22:11:19 [t1] c.Test1 - running
 */