package com.dhcc.thread;

/**
 * 创建线程方式二
 * 1：定义自定义类实现runable接口
 * 2：重写run方法
 * 3：创建自定义对象
 * 4：创建thread对象的时候 作为构造方法的参数进行传递
 * 5：启动线程start
 */
public class Demo02 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("main线程开启");
        //创建线程任务对象
        MyRunable myRunable = new MyRunable();
        //创建线程对象
        Thread t1 = new Thread(myRunable,"小强");
        Thread t2 = new Thread(myRunable,"旺财");

        System.out.println(t1.getState());
        System.out.println(t2.getState());


        t1.start();
        t2.start();
        Thread.sleep(5000);
        System.out.println(t1.getState());
        System.out.println(t2.getState());
    }
}
