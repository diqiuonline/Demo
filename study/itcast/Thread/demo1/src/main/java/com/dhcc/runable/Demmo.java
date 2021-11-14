package com.dhcc.runable;

/**
 * 可运行状态
 *      当线程有资格运行 并调用了start方法 线程首先进入可运行状态
 *      这种可运行状态，并不一定被线程调度运行
 *      简单来说 调用start方法之后 该线程依然是可运行状态 但未运行
 *      存放地 可运行池
 *      线程在运行的过程中，自然该线程也是可运行状态
 *
 *      jdk中可运行的线程转台，处于可运行中的某一线程正在jvm中运行
 *      但他可能正在等待系统的其他资源，比如处理器，io
 */
public class Demmo {

    public static void main(String[] args) {
        //创建线程对象
        MyThread myThread = new MyThread();
        System.out.println("创建完对象之后" + myThread.getState());

        myThread.start();

        System.out.println(" 开启了线程之后"+myThread.getState());
    }
}
