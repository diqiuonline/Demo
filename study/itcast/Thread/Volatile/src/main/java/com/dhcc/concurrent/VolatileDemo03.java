package com.dhcc.concurrent;

/**
 * 李锦卓
 * 2022/2/14 22:21
 * 1.0
 *      研究volation的原子性操作
 *       基本观点 Volatile 不能保证原子性操作
 *
 *      演示案例
 *          定义一个共享变量
 *          开启100个线程 每个线程累加10000次
 */
public class VolatileDemo03 {
    public static void main(String[] args) {
        Runnable target = new ThreadTarget();
        for (int i = 0; i < 100; i++) {
            new Thread(target,"第"+i+"个线程").start();
        }
    }
}


class ThreadTarget implements Runnable {
    private volatile   int count = 0;
    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
                count++;
             System.out.println(Thread.currentThread().getName()+"count ===========>"+count);
        }
    }
}
