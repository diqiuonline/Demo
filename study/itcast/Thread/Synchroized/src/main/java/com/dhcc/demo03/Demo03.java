package com.dhcc.demo03;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 李锦卓
 * 2022/2/6 20:26
 * 1.0
 *      目标 演示lock不可中断和可 中断
 */
public class Demo03 {
    private static Lock lock = new ReentrantLock();


    public static void main(String[] args) throws InterruptedException {
        Demo03.test02();
    }
    //演示lock不可终端
    public static void  test01() throws InterruptedException {
        Runnable run = () ->{
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName()+"获得锁，进入锁执行");
                Thread.sleep(88888);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName()+"释放锁");
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

        Thread.sleep(1000);
        System.out.println(t1.getState());
        System.out.println(t2.getState());
    }

    public static void  test02() throws InterruptedException {
        Runnable run = () ->{
            boolean b = false;
            try {
                b = lock.tryLock(3, TimeUnit.SECONDS);
                if (b) {
                    System.out.println(Thread.currentThread().getName() + "获得锁，进入锁执行");
                    Thread.sleep(88888);
                } else {
                    System.out.println(Thread.currentThread().getName() +"在指定的时间内没有得到锁 做其他操作");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (b) {
                    lock.unlock();
                    System.out.println(Thread.currentThread().getName()+"释放锁");
                }
            }

        };
        Thread t1 = new Thread(run);
        t1.start();

        Thread.sleep(1000);


        Thread t2 = new Thread(run);
        t2.start();

       /* System.out.println("停止第二个线程前");
        t2.interrupt();
        System.out.println("停止第二个线程后");

        Thread.sleep(1000);
        System.out.println(t1.getState());
        System.out.println(t2.getState());*/
    }
}
