package com.dhcc.thread;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * 李锦卓
 * 2022/2/26 11:42
 * 1.0
 */
public class ThreadCreateDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //方式一  继承thread类
       /* //创建自定义线程实例
        MyThread myThread = new MyThread();
        //启动线程
        myThread.start();
        //在main主线程打印信息
        for (int i = 0; i < 10; i++) {
            System.out.println("main主线程执行了"+new Date().getTime());
        }*/
        //方式二 实现runable接口
        //在main主线程打印信息
        /*for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "执行了" + new Date().getTime() + ", 执行次数是" + i);
        }
        //通过thread类执行runbale类
        Thread thread = new Thread(new MyRunable(),"mythread");
        thread.start();*/
        //方式三 实现callable接口实现
        // 创建futuretask实例 创建mycallable实例
        /*FutureTask<String> task = new FutureTask<>(new MyCallable());
        // 创建thread实例 执行futuretask
        Thread thread = new Thread(task,"MyCallable");
        thread.start();
        //在主线程打印信息
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "执行了" + new Date().getTime() + ", 执行次数是" + i);
        }
        // 获取并打印callable执行结果
        String s = task.get();
        System.out.println("MyCalle执行结果"+s);*/
        //使用线程吃创建
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        //使用executors获取线程池对象
        executorService.execute(new MyRunable());
        // 通过线程池对象获取线程并执行NyRunnable实例
        // 主线程打印信息
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "执行了" + new Date().getTime() + ", 执行次数是" + i);
        }

    }
}
