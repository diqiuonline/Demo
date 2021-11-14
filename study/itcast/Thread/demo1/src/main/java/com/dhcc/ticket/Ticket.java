package com.dhcc.ticket;

/**
 * synchronized(锁对象）{}
 *
 * 可以是任意类型
 * 互斥的线程 需要使用同一把锁
 */
public class Ticket implements Runnable{
    //成员位置 定义 票
    int ticket = 100;
    Object obj = new Object();
    @Override
    public void run() {
        //模拟窗口买票

        while (true) { //卖票运行
            synchronized (obj) {
                if (ticket > 0) {
                    //使用sleep 增加程序的时间 增加出票时间
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String name = Thread.currentThread().getName();
                    System.out.println(name+"正在卖"+ticket); //t1 97 还没--
                    //t2 97 还没 --
                    ticket--;
                }
            }

        }
    }
}
