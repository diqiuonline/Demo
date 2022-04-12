package com.dhcc.day1;

import java.util.concurrent.TimeUnit;

/**
 * 李锦卓
 * 2022/3/29 21:07
 * 1.0
 */
public class Sleeper {
    public static void sleep(int i) {
        try {
            TimeUnit.SECONDS.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleep(double i) {
        try {
            TimeUnit.MILLISECONDS.sleep((int) (i * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
