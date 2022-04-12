package com.dhcc.day1;

import lombok.extern.slf4j.Slf4j;

/**
 * 李锦卓
 * 2022/3/21 22:12
 * 1.0
 */
@Slf4j(topic = "c.Test2")
public class Test2 {
    public static void main(String[] args) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                log.debug("running");
            }
        };
        Thread thread = new Thread(r, "t2");
        thread.start();
    }
}
/**
 * 22:14:07 [t2] c.Test2 - running
 */