package com.dhcc.thread;

/**
 * 李锦卓
 * 2022/2/26 23:48
 * 1.0
 */
public class TicketSaleMain {
    public static void main(String[] args) {
        // 创建电影票对象
        Ticket ticket = new Ticket();
        // 创建thread对象 执行电影票售卖
        Thread thread = new Thread(ticket,"窗口1");
        Thread thread2 = new Thread(ticket,"窗口2");
        Thread thread3 = new Thread(ticket,"窗口3");
        //开始售卖
        thread.start();
        thread2.start();
        thread3.start();
    }
}
