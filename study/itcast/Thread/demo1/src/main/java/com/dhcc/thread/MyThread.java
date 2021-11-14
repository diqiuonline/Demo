package com.dhcc.thread;

/**
 * 自定义线程类
 */
public class MyThread extends Thread {
    public MyThread(String name) {
        super(name);
    }
    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            System.out.println(getName()+""+i);
        }
    }
}
