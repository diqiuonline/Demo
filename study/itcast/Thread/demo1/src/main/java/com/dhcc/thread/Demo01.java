package com.dhcc.thread;

/**
 * 开启线程方式一
 * 1：创建自定义类继承thread类
 * 2：重写run方法（run方法就是新的线程要执行的代码）
 * 3：创建自定义类对象（线程对象）
 * 4：调用start方法 就开启了新的线程
 */
public class Demo01 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("main线程开启");
        //创建线程对象
        MyThread myThread = new MyThread("小强");
        MyThread myThread2 = new MyThread("旺财");
        System.out.println(myThread.getState());
        System.out.println(myThread2.getState());
        myThread.start();
        myThread2.start();
        /*for (int i = 0; i < 20; i++) {
            System.out.println("小明"+i);
        }*/


        Thread.sleep(5000);
        System.out.println(myThread.getState());
        System.out.println(myThread2.getState());

    }
}
