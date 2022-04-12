package com.dhcc.day2;

import lombok.extern.slf4j.Slf4j;

/**
 * 李锦卓
 * 2022/3/30 21:22
 * 1.0
 */
@Slf4j(topic = "c.Demo03Test17")
public class Demo03Test17 {
    static int count = 0;
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                count++;
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                count--;
            }
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.debug("{}",count);
    }
}
/**
 * 21:25:05.303 [main] c.Demo03Test17 - 64
 */