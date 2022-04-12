package com.dhcc.day1;

import lombok.extern.slf4j.Slf4j;

/**
 * 李锦卓
 * 2022/3/22 11:28
 * 1.0
 */
@Slf4j(topic = "c.TestFrams")
public class TestFrams {
    public static void main(String[] args) {
        Thread t1 = new Thread(){
            @Override
            public void run() {
                method1(20);
            }
        };
        t1.setName("t1");
        t1.start();
        method1(10);
    }

    private static void method1(int x) {
        int y = x + 1;
        Object m = method2();
        log.debug(m.toString());
    }

    private static Object method2() {
        Object n = new Object();
        return n;
    }
}
/**
 * 11:32:11 [main] c.TestFrams - java.lang.Object@63e31ee
 * 11:32:11 [t1] c.TestFrams - java.lang.Object@304b3aa
 */
