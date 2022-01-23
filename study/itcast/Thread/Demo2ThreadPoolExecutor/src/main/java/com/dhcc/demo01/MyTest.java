package com.dhcc.demo01;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 测试类
 * 1：创建线程池类对象
 * 2：提交多个任务
 */
public class MyTest {
    public static void main2(String[] args) {
        //创建线程池类对象
        MyThreadPool myThreadPool = new MyThreadPool(2,4,20);
        
        //提交多个任务
        for (int i = 0; i < 30; i++) {
            //创建任务对象并提交线程池
            MyTask myTask = new MyTask(i);
            myThreadPool.submit(myTask);
        }
    }

    public static void main(String[] args) {
        List<Runnable> task = Collections.synchronizedList(new LinkedList<>());
        MyTask myTask = new MyTask(1);
        task.add(myTask);
        new MyWorker("核心线程" + 1, task).start();
        /*Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


            }
        });
        new Thread();*/
    }
}
