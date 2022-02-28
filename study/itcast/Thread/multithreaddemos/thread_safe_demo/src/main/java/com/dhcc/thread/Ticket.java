package com.dhcc.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 李锦卓
 * 2022/2/26 23:44
 * 1.0
 */
public class Ticket implements Runnable {
    private int ticketNu = 100; // 电影票的数量 默认100张
    private Object obj = new Object();
    private Lock lock = new ReentrantLock(true);  //是否公平 true 公平锁 多个线程都公平的拥有执行全 false 非公平  独占锁 默认值
    @Override
    public void run() {
        while (true) {
            lock.lock();
            try {
                if (ticketNu > 0) { //判断票数是否大于零
                    //有票 让线程睡眠100mm
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //打印当前售出的票数字和线程名
                    System.out.println("线程"+Thread.currentThread().getName() + "出售票" + ticketNu--);
                }
            }finally {
                lock.unlock();
            }

        }
    }

    private synchronized void saleTicket(){
        if (ticketNu > 0) { //判断票数是否大于零
            //有票 让线程睡眠100mm
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //打印当前售出的票数字和线程名
            System.out.println("线程"+Thread.currentThread().getName() + "出售票" + ticketNu--);
        }
        //票数-1
    }
}
