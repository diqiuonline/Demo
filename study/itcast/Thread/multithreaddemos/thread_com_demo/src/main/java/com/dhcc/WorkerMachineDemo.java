package com.dhcc;

import java.util.Date;
import java.util.concurrent.Semaphore;

/**
 * 李锦卓
 * 2022/2/28 23:21
 * 1.0
 */
public class WorkerMachineDemo {
    static  class  Work implements Runnable{
        private int workerNum;  //工号
        private Semaphore semaphore; //机器数

        public Work(int workerNum, Semaphore semaphore) {
            this.workerNum = workerNum;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                //工人去获取机器
                semaphore.acquire();
                //打印工人获取到机器 开始工作
                System.out.println(Thread.currentThread().getName()+"获取到机器 开始工作"+new Date().getTime());
                //线程睡眠1000毫秒 模拟工人使用机器
                Thread.sleep(1000);
                //使用完毕 释放机器 打印工人使用完毕 释放机器
                semaphore.release();
                System.out.println(Thread.currentThread().getName()+"工作完毕 释放机器"+new Date().getTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    public static void main(String[] args) {
        int workers = 8; //代表工人数 8个
        Semaphore semaphore = new Semaphore(3); //机器数 三个

        for (int i = 0; i < workers; i++) {
            new Thread(new Work(i, semaphore)).start();
        }

    }
}
/**
 * Thread-2获取到机器 开始工作1646062377685
 * Thread-0获取到机器 开始工作1646062377685
 * Thread-1获取到机器 开始工作1646062377685
 * Thread-2工作完毕 释放机器1646062378695
 * Thread-3获取到机器 开始工作1646062378695
 * Thread-4获取到机器 开始工作1646062378695
 * Thread-1工作完毕 释放机器1646062378695
 * Thread-0工作完毕 释放机器1646062378695
 * Thread-5获取到机器 开始工作1646062378695
 * Thread-4工作完毕 释放机器1646062379703
 * Thread-6获取到机器 开始工作1646062379703
 * Thread-5工作完毕 释放机器1646062379703
 * Thread-7获取到机器 开始工作1646062379703
 * Thread-3工作完毕 释放机器1646062379703
 * Thread-6工作完毕 释放机器1646062380714
 * Thread-7工作完毕 释放机器1646062380714
 */