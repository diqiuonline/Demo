package com.dhcc.day1;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * 李锦卓
 * 2022/3/22 22:39
 * 1.0
 */
@Slf4j(topic = "c.Test14")
public class Test14 {
    public static void main(String[] args) throws InterruptedException {
        test4();
    }
    private static void test3() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("park...");
            LockSupport.park();
            log.debug("unpark...");
            log.debug("打断状态：{}",Thread.currentThread().isInterrupted());
        }, "t1");
        t1.start();
        /**
         * 22:41:43.084 [t1] c.Test14 - park...
         */
        Thread.sleep(1000);
        t1.interrupt();
        /**
         * 22:43:14.998 [t1] c.Test14 - park...
         * 22:43:15.998 [t1] c.Test14 - unpark...
         * 22:43:15.998 [t1] c.Test14 - 打断状态：true
         */
    }
    private static void test4() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("park...");
            LockSupport.park();
            log.debug("unpark...");
            log.debug("打断状态：{}", Thread.interrupted());

            LockSupport.park();
            log.debug("unpark...");
        }, "t1");
        t1.start();

        Thread.sleep(1000);
        t1.interrupt();
        /**
         * 22:48:33.978 [t1] c.Test14 - park...
         * 22:48:34.987 [t1] c.Test14 - unpark...
         * 22:48:34.987 [t1] c.Test14 - 打断状态：true
         */
    }
}
