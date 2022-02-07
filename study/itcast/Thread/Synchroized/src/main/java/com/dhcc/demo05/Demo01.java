package com.dhcc.demo05;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 李锦卓
 * 2022/2/5 16:56
 * 1.0
 * 演示原子性问题
 * 定义一个共享变量   numnrt
 * 会number进行1000次++操作
 * 使用5个线程来进行
 */
public class Demo01 {
    private static AtomicInteger atomicInteger = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        Runnable increment = () -> {
                for (int i = 0; i < 1000; i++) {
                    atomicInteger.incrementAndGet();
                }
        };
        List<Thread> list = new ArrayList<>();
        //使用5个线程来进行
        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(increment);
            t.start();
            list.add(t);
        }
        for (Thread t : list) {
            t.join();
        }
        System.out.println("number = "+atomicInteger.get());
    }
}
