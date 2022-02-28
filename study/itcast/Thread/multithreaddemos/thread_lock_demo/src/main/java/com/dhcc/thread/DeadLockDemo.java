package com.dhcc.thread;

/**
 * 李锦卓
 * 2022/2/27 14:46
 * 1.0
 */
public class DeadLockDemo {
    public static void main(String[] args) {
        //创建两个runnable对象 DeadLoacRunnable实例 flag = 1 、flag=2
        DeadLockRunnable runnable1 = new DeadLockRunnable(1);
        DeadLockRunnable runnable2 = new DeadLockRunnable(2);
        // 启动创建两个线程 执行两个DeadLockRunnable实例
        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
        //两个线程同时启动
        thread1.start();
        thread2.start();
    }
}
