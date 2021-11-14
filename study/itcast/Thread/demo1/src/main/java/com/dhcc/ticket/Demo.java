package com.dhcc.ticket;

/**
 * 模拟买票操作
 *      假设一场电影有100张票
 *      三个窗口同时卖票
 *
 *      窗口 线程对象
 *      卖票 线程任务 实现runnable接口
 */
public class Demo {
    public static void main(String[] args) {
        //创建买票任务对象
        Ticket ticket = new Ticket();

        //创建三个窗口
        Thread t1 = new Thread(ticket, "窗口1");
        Thread t2 = new Thread(ticket, "窗口2");
        Thread t3 = new Thread(ticket, "窗口3");

        //开启
        t1.start();
        t2.start();
        t3.start();


    }
}
