package com.dhcc;

import java.util.concurrent.CountDownLatch;

/**
 * 李锦卓
 * 2022/2/28 22:23
 * 1.0
 */
public class CoachRacerDemo {
    private CountDownLatch countDownLatch = new CountDownLatch(3); //设置要等待的运动员是三个
    //运动员方法 由运动员线程调用
    public void racer() {
        System.out.println("" + Thread.currentThread().getName() +"正在准备 。。。。");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() +"准备完毕 。。。。");
        countDownLatch.countDown(); //
    }
    /**
     * 教练方法 由教练线程调用
     */
    public void coach()  {
        //获取教练线程的名称
        String name = Thread.currentThread().getName();
        //教练等待所有运动员准备完毕
        System.out.println(name+"等待运动员准备");
        //调用countdownlatch的await方法 等待其他线程执行完毕
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //所有运动员已就绪，教练开始训练 打印训练信息
        System.out.println("所有运动员已就绪" + name + "教练开始训练");
    }

    public static void main(String[] args) {
        //创建coachracerdemo实例
        CoachRacerDemo coachRacerDemo = new CoachRacerDemo();
        //创建三个运动员实例 调用racer方法
        Thread t1 = new Thread(() ->{
            coachRacerDemo.racer();
        },"运动员1");
        Thread t2 = new Thread(() ->{
            coachRacerDemo.racer();
        },"运动员2");
        Thread t3 = new Thread(() ->{
            coachRacerDemo.racer();
        },"运动员3");
        //创建一个教练实例 调用coach发给发
        Thread t4 = new Thread(() ->{
            coachRacerDemo.coach();
        },"教练");
        t4.start();
        t1.start();
        t2.start();
        t3.start();
    }
}
/**
 * 教练等待运动员准备
 * 运动员3正在准备 。。。。
 * 运动员2正在准备 。。。。
 * 运动员1正在准备 。。。。
 * 运动员1准备完毕 。。。。
 * 运动员2准备完毕 。。。。
 * 运动员3准备完毕 。。。。
 * 所有运动员已就绪教练教练开始训练
 */