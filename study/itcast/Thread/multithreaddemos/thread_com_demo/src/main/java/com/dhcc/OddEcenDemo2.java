package com.dhcc;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 李锦卓
 * 2022/2/28 21:03
 * 1.0
 */
public class OddEcenDemo2 {
    private int i = 0; //要打印的书
    //private Object obj = new Object();
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();


    /**
     * 奇书打印方法 有奇书线程调用
     */
    public void odd() throws InterruptedException {
        //判断 i是否小于10 打印奇书
        while (i < 10) {
            lock.lock();
            try {
                if (i % 2 == 1) {
                    System.out.println("奇书：" + i++);
                    condition.signal(); //唤醒偶数线程打印
                } else {
                    condition.await();// 等待偶数线程打印完毕
                }
            }finally {
                lock.unlock();
            }

        }
    }

    /**
     * 偶数打印方法由偶数线程调用
     */
    public void even() throws InterruptedException {
        //判断 i是否小于10 打印奇书
        while (i < 10) {
            lock.lock();
            try {
                if (i % 2 == 0) {
                    System.out.println("偶数：" + i++);
                    condition.signal(); //唤醒偶数线程打印
                } else {
                    condition.await();// 等待偶数线程打印完毕
                }
            }finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        final OddEcenDemo2 oddEcenDemo = new OddEcenDemo2();
        //等待奇数线程打印
        Thread thread1 = new Thread(() -> {
            try {
                oddEcenDemo.odd();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        //等待偶数线程打印
        Thread thread2 = new Thread(() -> {
            try {
                oddEcenDemo.even();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        thread2.start();
    }
}
/*8
偶数：0
奇书：1
偶数：2
奇书：3
偶数：4
奇书：5
偶数：6
奇书：7
偶数：8
奇书：9
 */