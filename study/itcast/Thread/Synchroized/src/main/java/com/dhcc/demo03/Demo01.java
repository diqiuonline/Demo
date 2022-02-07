package com.dhcc.demo03;

import java.nio.channels.NotYetBoundException;

/**
 * 李锦卓
 * 2022/2/6 17:47
 * 1.0
 *
 *      目标演示synchronized 可重入
 *       自定义一个线程类
 *       在线程类的run方法中使用嵌套的同步代码块
 *         使用两个线程来执行
 */
public class Demo01 {
    public static void main(String[] args) {
        new MyThread().start();
        new MyThread().start();
    }


    public static void test01() {
        synchronized (MyThread.class) {
            System.out.println(Thread.currentThread().getName() + "进入了同步代码快2");
        }
    }
}


//自定义各一个线程类
class MyThread extends Thread {
    @Override
    public void run() {
        synchronized (MyThread.class) {
            System.out.println(getName() + "进入了同步代码快1");
           Demo01.test01();
        }
    }


/*    public void test01() {
        synchronized (MyThread.class) {
            System.out.println(getName() + "进入了同步代码快2");
        }
    }*/

}
