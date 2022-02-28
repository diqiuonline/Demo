package com.dhcc;

import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 李锦卓
 * 2022/2/28 22:35
 * 1.0
 */
public class ThreeThreadStartDemo {
    private CyclicBarrier cyclicBarrier = new CyclicBarrier(3);  //参与cyclicbarrier的线程数
    private void startThread() {
        //打印线程准备西东
        String name = Thread.currentThread().getName();
        System.out.println(name+"正在准备。。。。。"+ new Date().getTime());
        //调用cyclicbarriar的await方法 等待线程全部准备完成
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        //打印线程启动完毕信息
        System.out.println(name + "已经启动完毕" + new Date().getTime());

    }

    public static void main(String[] args) {
        ThreeThreadStartDemo threeThreadStartDemo = new ThreeThreadStartDemo();

        new Thread(() ->{
            threeThreadStartDemo.startThread();
        }).start();
        new Thread(() ->{
            threeThreadStartDemo.startThread();
        }).start();
        new Thread(() ->{
            threeThreadStartDemo.startThread();
        }).start();
    }
}
/**
 * Thread-0正在准备。。。。。1646060673001
 * Thread-2正在准备。。。。。1646060673001
 * Thread-1正在准备。。。。。1646060673001
 * Thread-1已经启动完毕1646060673002
 * Thread-2已经启动完毕1646060673002
 * Thread-0已经启动完毕1646060673002
 */