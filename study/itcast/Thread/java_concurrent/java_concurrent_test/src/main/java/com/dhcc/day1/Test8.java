package com.dhcc.day1;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 李锦卓
 * 2022/3/22 15:00
 * 1.0
 */
@Slf4j(topic = "c.Test8")
public class Test8 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        log.debug("enter");
        TimeUnit.SECONDS.sleep(1);
        log.debug("end");
    }
}
/**
 * 15:02:06 [main] c.Test8 - enter
 * 15:02:07 [main] c.Test8 - end
 */