package com.dhcc.day1;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * 李锦卓
 * 2022/3/22 14:53
 * 1.0
 */
@Slf4j(topic = "c.Test7")
public class Test7 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("enter sleep ....");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    log.debug("wake up ....");
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        Thread.sleep(1000);
        log.debug("interrupt .... ");
        t1.interrupt();
    }
}
/**
 * 14:58:30 [t1] c.Test7 - enter sleep ....
 * 14:58:31 [main] c.Test7 - interrupt ....
 * 14:58:31 [t1] c.Test7 - wake up ....
 * java.lang.InterruptedException: sleep interrupted
 * 	at java.lang.Thread.sleep(Native Method)
 * 	at com.dhcc.day1.Test7$1.run(Test7.java:20)
 */