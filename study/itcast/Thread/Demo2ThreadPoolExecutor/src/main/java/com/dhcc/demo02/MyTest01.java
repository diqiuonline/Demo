package com.dhcc.demo02;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 *联系Executors 获取ExecutorService 然后调用方法 提交任务
 */
public class MyTest01 {
    public static void main(String[] args) {
        //test1();
        test2();



    }
    // 联系newCacheThreadPool方法
    private static void test1() {
        //1 使用工厂类，获取线程池对象
        ExecutorService executorService = Executors.newCachedThreadPool();
        //2 提交任务
        for (int i = 0; i < 10; i++) {
            executorService.submit(new MtRunnable(i));
        }
    }


    // 联系newCacheThreadPool（ThreadFactory）方法
    private static void test2() {
        //1 使用工厂类，获取线程池对象

        ExecutorService executorService = Executors.newCachedThreadPool(new ThreadFactory() {
            int n = 1;
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "自定义的线程名称" + n++);
            }
        });
        //2 提交任务
        for (int i = 0; i < 10; i++) {
            executorService.submit(new MtRunnable(i));
        }
    }
}

/**
 * 任务类 包含一个任务编号 在任务中 打印出是哪一个线程正在执行任务
 */
class MtRunnable implements Runnable {
    private int id;

    public MtRunnable(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        //获取线程的名称 答应一句话
        String name = Thread.currentThread().getName();
        System.out.println(name+"执行了任务"+id);
    }
}