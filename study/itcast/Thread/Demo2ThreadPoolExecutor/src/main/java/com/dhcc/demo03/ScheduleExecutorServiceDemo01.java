package com.dhcc.demo03;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2022/2/5 11:02
 * <p>
 * 测试ScheduleExecutorService接口中的延迟执行任何和城府执行任务的功能
 */
public class ScheduleExecutorServiceDemo01 {
    public static void main(String[] args) {
        //1 获取一个具备延迟执行任务的线程池对象
        ScheduledExecutorService es = Executors.newScheduledThreadPool(3);
        //2 创建多个任务对象 提交任务 每个任务延迟两秒执行
        for (int i = 0; i < 10; i++) {
            es.schedule(new MyRunnable(i), 2, TimeUnit.SECONDS);
        }
        System.out.println("over");
    }
}

class MyRunnable implements Runnable {
    private int id;

    public MyRunnable(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println(name + "执行了任务：" + id);

    }
}

