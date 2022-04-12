package com.dhcc.day1;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 李锦卓
 * 2022/3/21 22:22
 * 1.0
 */
@Slf4j(topic = "c.Test5")
public class Test5 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("running ....");
            }
        };
        //t1.run();  //11:59:07 [main] c.Test5 - running .... 主线程执行的
        System.out.println(t1.getState());
        t1.start();
        System.out.println(t1.getState());

    }
}
/**
 * NEW
 * RUNNABLE
 * 14:31:46 [t1] c.Test5 - running ....
 */