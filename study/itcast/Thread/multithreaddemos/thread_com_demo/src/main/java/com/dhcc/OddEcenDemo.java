package com.dhcc;

/**
 * 李锦卓
 * 2022/2/28 21:03
 * 1.0
 */
public class OddEcenDemo {
    private int i = 0; //要打印的书
    private Object obj = new Object();


    /**
     * 奇书打印方法 有奇书线程调用
     */
    public void odd() throws InterruptedException {
        //判断 i是否小于10 打印奇书
        while (i < 10) {
            synchronized (obj) {
                if (i % 2 == 1) {
                    System.out.println("奇书：" + i++);
                    obj.notify(); //唤醒偶数线程打印
                } else {
                    obj.wait();// 等待偶数线程打印完毕
                }
            }

        }
    }

    /**
     * 偶数打印方法由偶数线程调用
     */
    public void even() throws InterruptedException {
        //判断 i是否小于10 打印奇书
        while (i < 10) {
            synchronized (obj) {
                if (i % 2 == 0) {
                    System.out.println("偶数：" + i++);
                    obj.notify(); //唤醒奇数线程打印
                } else {
                    obj.wait();// 等待奇数线程打印完毕
                }
            }
        }
    }

    public static void main(String[] args) {
        final  OddEcenDemo oddEcenDemo = new OddEcenDemo();
        //等待奇数线程打印
        Thread thread1 = new Thread(() ->{
            try {
                oddEcenDemo.odd();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        //等待偶数线程打印
        Thread thread2 = new Thread(() ->{
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
