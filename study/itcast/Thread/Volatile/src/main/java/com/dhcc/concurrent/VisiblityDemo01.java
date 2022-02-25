package com.dhcc.concurrent;

/**
 * 李锦卓
 * 2022/2/8 16:04
 * 1.0
 *      目标 研究多线程下变量访问的不可见性
 *
 *      准备内容
 *          准备两个线程
 *          定义一个成员变量
 *          开启两个线程 其中一个负责修改 另一个负责拂去
 */
public class VisiblityDemo01 {
    public static void main(String[] args) {
        //开启一个子线程
        MyThread t = new MyThread();
        t.start();

        //主线程
        while (true) {
            if (t.isFlag()) {
                System.out.println("主线程进入循环执行");
            }
        }
    }
}

class MyThread extends Thread {
    //成员变量
    private boolean flag = false;

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