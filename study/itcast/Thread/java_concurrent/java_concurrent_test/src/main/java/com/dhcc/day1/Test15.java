package com.dhcc.day1;

import lombok.extern.slf4j.Slf4j;

/**
 * 李锦卓
 * 2022/3/23 14:34
 * 1.0
 */
@Slf4j(topic = "c.Test15")
public class Test15 {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() ->{
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
            log.debug("结束");
        },"t1");
        t1.setDaemon(true);
        /**
         * 22:28:59.285 [main] c.Test15 - 结束
         *
         * 进程已结束,退出代码0
         */
        t1.start();
        /**
         * 22:28:59.285 [main] c.Test15 - 结束cccc
         */

        Thread.sleep(1000);
        log.debug("结束");
    }
}
