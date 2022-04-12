package com.dhcc.day1;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.extern.slf4j.Slf4j;

/**
 * 李锦卓
 * 2022/3/22 18:09
 * 1.0
 */
@Slf4j(topic = "c.Test12")
public class Test12 {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                boolean interrupted = Thread.currentThread().isInterrupted();
                if (interrupted) {
                    log.debug("被打断了 推出循环");
                    break;
                    /**
                     * 19:55:08.850 [main] c.Test12 - interrupt
                     * 19:55:08.853 [t1] c.Test12 - 被打断了 推出循环
                     */
                }
            }
        },"t1");
        t1.start();
        Thread.sleep(1000);
        log.debug("interrupt");
        t1.interrupt();
    }
}
