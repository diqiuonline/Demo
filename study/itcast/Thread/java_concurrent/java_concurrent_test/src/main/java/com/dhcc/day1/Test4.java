package com.dhcc.day1;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 李锦卓
 * 2022/3/21 22:22
 * 1.0
 */
@Slf4j(topic = "c.Test4")
public class Test4 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> task = new FutureTask(() -> {
            log.debug("running");
            Thread.sleep(1000);
            return 100;
        });
        Thread t = new Thread(task,"t1");
        t.start();
        Integer integer = task.get();
        log.debug("{}",integer);

    }
}
/**
 * 222:26:59 [t1] c.Test4 - running
 * 22:27:00 [main] c.Test4 - 100}
 */