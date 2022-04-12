package com.dhcc.day1;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * 李锦卓
 * 2022/3/21 22:22
 * 1.0
 */
@Slf4j(topic = "c.Test6")
public class Test6 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        log.debug(t1.getState().toString());
        Thread.sleep(500);
        log.debug(t1.getState().toString());
    }
}
/**
 * 14:50:11 [main] c.Test6 - RUNNABLE
 * 14:50:12 [main] c.Test6 - TIMED_WAITING
 */