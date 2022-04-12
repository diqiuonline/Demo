package com.dhcc.day1;

import lombok.extern.slf4j.Slf4j;

/**
 * 李锦卓
 * 2022/3/21 22:29
 * 1.0
 */
@Slf4j(topic = "c.TestMultiThread")
public class TestMultiThread {
    public static void main(String[] args) {
        new Thread(()->{
            while (true) {
                log.debug("running");
            }
        },"t1").start();
        new Thread(()->{
            while (true) {
                log.debug("running");
            }
        },"t2").start();
    }
}
/**
 * 22:32:33 [t2] c.TestMultiThread - running
 * 22:32:33 [t1] c.TestMultiThread - running
 * 22:32:33 [t1] c.TestMultiThread - running
 * 22:32:33 [t1] c.TestMultiThread - running
 * 22:32:33 [t2] c.TestMultiThread - running
 * 22:32:33 [t2] c.TestMultiThread - running
 * 22:32:33 [t2] c.TestMultiThread - running
 * 22:32:33 [t2] c.TestMultiThread - running
 * 22:32:33 [t2] c.TestMultiThread - running
 */