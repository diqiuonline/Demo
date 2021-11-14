package com.dhcc.waiting;

/**
 * 无限等待
 * object中的wait方法完成
 * 使当前线程 进入无线等待状态 直到其他线程有唤notify 或者 notifyall 才能被唤醒
 *
 * 线程间通信 两个线程执行不同的操作 关联的
 *  连个线程 使用同样的锁 只能使用锁对象调用wait方法或notify方法
 */
public class WaitingTest {
    private static Object obj = new Object();


    public static void main(String[] args) {
        Thread t1 = new Thread(){
            @Override
            public void run() {
                synchronized (obj) {
                    System.out.println("调用wait方法，当前线程进入无限等待状态，等待着别的线程来唤醒");
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("唤醒了这个状态 就不是无限等待了 线程执行完毕");
                }
            }
        };
        t1.start();
        try {
            Thread.sleep(3000);
            System.out.println("耗时3s");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("t1的线程状态："+t1.getState());

        new Thread(){
            @Override
            public void run() {
                synchronized (obj) {
                    try {
                        Thread.sleep(2000);
                        System.out.println("耗时2s");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("执行唤醒操作");
                    obj.notifyAll();
                }

            }
        }.start();
    }
}
