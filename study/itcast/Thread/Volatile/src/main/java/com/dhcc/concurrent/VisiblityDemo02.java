package com.dhcc.concurrent;

/**
 * 李锦卓
 * 2022/2/8 16:04
 * 1.0
 *      目标 解决多线程下并发修改的不可见性问题
 *
 *      解决思路
 *          加锁
 *          加关键字volatile
 */
public class VisiblityDemo02 {
    public static void main(String[] args) {
        //开启一个子线程
        MyThread2 t = new MyThread2();
        t.start();

        //主线程
        while (true) {
                if (t.isFlag()) {
                    System.out.println("主线程进入循环执行");
                }
        }
    }
}

class MyThread2 extends Thread {
    //成员变量
    private volatile boolean flag = false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flag = true;
        System.out.println("flag = "+flag);
    }
}