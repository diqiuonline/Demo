package com.dhcc.day1;

import lombok.extern.slf4j.Slf4j;

/**
 * 李锦卓
 * 2022/3/22 15:56
 * 1.0
 */
@Slf4j(topic = "c.Test10")
public class Test10 {
    static int r = 0;
    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        log.debug("开始");
        Thread t1 = new Thread(() -> {
            log.debug("开始");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("结束");
            r = 10;
        }, "t1");
        t1.start();
        /**
         * 15:58:44 [main] c.Test10 - 开始
         * 15:58:44 [main] c.Test10 - 结果未: 0
         * 15:58:44 [main] c.Test10 - 结束
         * 15:58:44 [t1] c.Test10 - 开始
         * 15:58:45 [t1] c.Test10 - 结束
         */
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         * 16:04:16.624 [main] c.Test10 - 开始
         * 16:04:16.703 [t1] c.Test10 - 开始
         * 16:04:17.707 [t1] c.Test10 - 结束
         * 16:04:17.707 [main] c.Test10 - 结果未: 10
         * 16:04:17.709 [main] c.Test10 - 结束
         */
        log.debug("结果未: {}", r);
        log.debug("结束");
    }
}

