package com.dhcc.demo03;

/**
 * 李锦卓
 * 2022/2/6 20:15
 * 1.0
 * 目标 演示synchronized不可中断
 * 1 定义一个runnable
 * 2 在runnable中定义同步代码块
 * 3 先开启一个线程 来执行同步代码块 保证不退出同步代码块
 * 4 后开启一个线程来执行同步代码块 （阻塞状态）
 * 5 停止第二个线程
 */
public class Demo02 {
    public static void main(String[] args) throws InterruptedException {
        Runnable run = () -> {
            synchronized (Demo02.class) {
                System.out.println(Thread.currentThread().getName() + "进入同步代码块");
                try {
                    Thread.sleep(888888);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t1 = new Thread(run);
        t1.start();
        Thread.sleep(1000);
        Thread t2 = new Thread(run);
        t2.start();
        System.out.println("停止第二个线程前");
        t2.interrupt();
        System.out.println("停止第二个线程后");

        System.out.println(t1.getState());
        System.out.println(t2.getState());
    }
}
