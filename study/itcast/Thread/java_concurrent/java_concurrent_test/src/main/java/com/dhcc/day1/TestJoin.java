package com.dhcc.day1;

import lombok.extern.slf4j.Slf4j;

/**
 * 李锦卓
 * 2022/3/22 17:19
 * 1.0
 */
@Slf4j(topic = "c.TestJoin")
public class TestJoin {
    static int r = 0;
    static int r1 = 0;
    static int r2 = 0;

    public static void main(String[] args) {
        try {
            //test2();
            test3();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test3() throws Exception {
        Thread t1 = new Thread(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            r1 = 10;
        });
        Long start = System.currentTimeMillis();
        t1.start();
        log.debug("join begin");
        t1.join(1500);
        Long end = System.currentTimeMillis();
        log.debug("r1: {} r2: {} cost: {}", r1, r2, end - start);
        /**
         * 17:37:17.845 [main] c.TestJoin - join begin
         * 17:37:19.349 [main] c.TestJoin - r1: 0 r2: 0 cost: 1506
         */

    }

    private static void test2() throws Exception {
        Thread t1 = new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            r1 = 10;
        });
        Thread t2 = new Thread(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            r2 = 20;
        });
        Long start = System.currentTimeMillis();
        t1.start();
        t2.start();
      /*  log.debug("join begin");
        t1.join();
        log.debug("t1 join end");
        t2.join();
        log.debug("t2 join end");
        Long end = System.currentTimeMillis();
        log.debug("r1: {} r2: {} cost: {}", r1, r2, end - start);*/
        /**
         * 17:24:19.106 [main] c.TestJoin - join begin
         * 17:24:20.114 [main] c.TestJoin - t1 join end
         * 17:24:21.117 [main] c.TestJoin - t2 join end
         * 17:24:21.118 [main] c.TestJoin - r1: 10 r2: 20 cost: 2014
         */
        log.debug("join begin");
        t2.join();
        log.debug("t2 join end");
        t1.join();
        log.debug("t1 join end");
        Long end = System.currentTimeMillis();
        log.debug("r1: {} r2: {} cost: {}", r1, r2, end - start);
        /**
         * 17:31:49.907 [main] c.TestJoin - join begin
         * 17:31:51.906 [main] c.TestJoin - t2 join end
         * 17:31:51.906 [main] c.TestJoin - t1 join end
         * 17:31:51.906 [main] c.TestJoin - r1: 10 r2: 20 cost: 2000
         */
    }
}
