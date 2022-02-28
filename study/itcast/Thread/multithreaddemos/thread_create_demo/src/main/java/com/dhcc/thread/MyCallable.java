package com.dhcc.thread;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * 李锦卓
 * 2022/2/26 22:35
 * 1.0
 */
public class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "执行了" + new Date().getTime() + ", 执行次数是" + i);
        }
        return "MyCallable 接口执行完成";
    }
}
