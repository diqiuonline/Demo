package com.dhcc.blocked;

/**
 * 线程状态指 锁阻塞状态blocked
 *  JDK：锁阻塞并且正在等待监视器锁的某一线程的线程状态
 *  处于受阻塞状态的某一线程正在等待监视器，以便进入一个同步代码块/同步方法
 *  还有就是 调用object.wait 方法 之后再次进入同步中时
 */
public class blocked {
    public static void main(String[] args) throws InterruptedException {
        //创建锁对象
        Object obj = new Object();
        //创建线程Ab
        A a = new A("小强", obj);
        B b = new B("旺财", obj);

        a.start();
        b.start();

        Thread.sleep(3000);
        System.out.println("耗时3s");
        System.out.println("线程A状态为："+a.getState());
        System.out.println("线程B状态为："+b.getState());


        Thread.sleep(8000);
        System.out.println("耗时8s");
        System.out.println("线程A状态为："+a.getState());
        System.out.println("线程B状态为："+b.getState());
    }
}
