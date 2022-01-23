package com.dhcc.demo01;

import java.util.List;

/**
 * 需求：
 * 编写一个线程类，继承thread类，设计一个属性，用于保存线程的名字
 * 设计一个集合，用于保存所有的任务。
 */
public class MyWorker extends Thread {
    private String name;
    private List<Runnable> tasks;

    public MyWorker(String name,  List<Runnable> tasks) {
        super(name);
        this.tasks = tasks;
    }

    @Override
    public void run() {
        //判断集合中是否有任务，只要有，就一直执行
        while (tasks.size() > 0) {
            Runnable r = tasks.remove(0);
            r.run();

        }
    }
}
