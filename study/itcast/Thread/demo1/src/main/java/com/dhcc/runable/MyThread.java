package com.dhcc.runable;

public class MyThread extends Thread {
    @Override
    public void run() {
        //线程执行代码
        System.out.println("线程开始执行");
        System.out.println("线程开始执行具体的任务");
        // 假设这个任务使用5s
        long beginTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - beginTime < 5000) {
            //假设任务做了5s
        }
        System.out.println("线程执行完毕");
    }
}
