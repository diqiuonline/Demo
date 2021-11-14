package com.dhcc.googkaike;


/**
 * 研究的是新建线程状态
 *      新建线程 至今还未启动的线程处于这一状态
 *      如果没有给线程起名字，那么默认就是thread-0 thread-1.。。
 */
public class ThreadStateTest1 {
    public static void main(String[] args) {
        //创建线程对象
        MyThread myThread = new MyThread();
        //System.out.println(myThread.getName());
        /*System.out.println("线程处于什么杨的状态" + myThread.getState());
        myThread.run();
        System.out.println("线程处于什么杨的状态" + myThread.getState());


        System.out.println("--------");*/



        myThread.start();



        System.out.println("线程start之后什么杨的状态" + myThread.getState());
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("开始6s后状态" + myThread.getState());
    }
}
