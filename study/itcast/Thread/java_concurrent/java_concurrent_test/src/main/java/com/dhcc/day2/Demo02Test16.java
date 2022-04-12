package com.dhcc.day2;

import com.dhcc.day1.Sleeper;
import lombok.extern.slf4j.Slf4j;

import static com.dhcc.day1.Sleeper.sleep;

/**
 * 李锦卓
 * 2022/3/29 21:05
 * 1.0
 */
@Slf4j(topic = "c.Demo02Test16")
public class Demo02Test16 {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            log.debug("洗水壶");
            sleep(1);
            log.debug("烧开水");
            sleep(5);
        },"老王");

        Thread t2 = new Thread(() -> {
            log.debug("洗茶壶");
            sleep(1);
            log.debug("洗茶叶");
            sleep(2);
            log.debug("拿茶叶");
            sleep(1);
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("泡茶");
        },"小王");

        t1.start();
        t2.start();
    }
}
/**
 * 21:13:29.442 [老王] c.Demo02Test16 - 洗水壶
 * 21:13:29.442 [小王] c.Demo02Test16 - 洗茶壶
 * 21:13:30.463 [小王] c.Demo02Test16 - 洗茶叶
 * 21:13:30.463 [老王] c.Demo02Test16 - 烧开水
 * 21:13:32.475 [小王] c.Demo02Test16 - 拿茶叶
 * 21:13:35.475 [小王] c.Demo02Test16 - 泡茶
 */