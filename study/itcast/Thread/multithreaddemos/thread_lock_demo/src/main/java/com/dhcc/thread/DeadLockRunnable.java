package com.dhcc.thread;

/**
 * 李锦卓
 * 2022/2/27 14:39
 * 1.0
 */
public class DeadLockRunnable implements Runnable {
    private int flag;  //决定线程走向的标记
    private static Object ob1 = new Object(); //锁对象1
    private static  Object ob2 = new Object(); //锁对象2


    public DeadLockRunnable (int flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        if (flag == 1) {
            //线程1 执行代码
            synchronized (ob1) {
                System.out.println(Thread.currentThread().getName()+"已获取到资源ob1，请求ob2");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (ob2) {
                    System.out.println(Thread.currentThread().getName()+"已获取到资源ob1 和 ob2");
                }
            }
        } else {
            //线程2 执行代码
            if (flag == 2) {
                //线程1 执行代码
                synchronized (ob2) {
                    System.out.println(Thread.currentThread().getName() + "已获取到资源ob2，请求ob1");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (ob1) {
                        System.out.println(Thread.currentThread().getName() + "已获取到资源ob2 和 ob1");
                    }
                }
            }
        }
    }
}
