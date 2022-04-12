package com.dhcc.day1;

import lombok.extern.slf4j.Slf4j;

/**
 * 李锦卓
 * 2022/3/21 22:12
 * 1.0
 */
@Slf4j(topic = "c.Test3")
public class Test3 {
    public static void main(String[] args) {
        Runnable r = ()->{log.debug("running");};
        Thread thread = new Thread(r, "t2");
        thread.start();
    }
}
/**
 * 22:14:07 [t2] c.Test3 - running
 */