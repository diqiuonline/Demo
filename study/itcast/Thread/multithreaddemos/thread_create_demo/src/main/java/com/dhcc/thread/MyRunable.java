package com.dhcc.thread;

import java.util.Date;

/**
 * 李锦卓
 * 2022/2/26 11:44
 * 1.0
 */
public class MyRunable implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "执行了" + new Date().getTime() + ", 执行次数是" + i);
        }
    }
}
