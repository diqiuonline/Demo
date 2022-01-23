package com.dhcc.demo02;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 *联系Executors 获取ExecutorService 然后调用方法 测试关闭线程池
 */
public class MyTest04 {
    public static void main(String[] args) {
        //test1();
        test2();



    }
    // 联系newCacheThreadPool方法
    private static void test1() {
        //1 使用工厂类，获取线程池对象
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        //2 提交任务
        for (int i = 0; i < 10; i++) {
            executorService.submit(new MtRunnable4(i));
        }
        //关闭线程池，不在接受新的任务，以前的任务还会继续执行
        executorService.shutdown();
        //executorService.submit(new MtRunnable4(888));
    }


    // 联系newCacheThreadPool（ThreadFactory）方法
    private static void test2() {
        //1 使用工厂类，获取线程池对象

        ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
            int n = 1;
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "自定义的线程名称" + n++);
            }
        });
        //2 提交任务
        for (int i = 0; i < 10; i++) {
            executorService.submit(new MtRunnable4(i));
        }
        //立刻关闭线程池 如果线程池中还有缓存的任务没有执行 则取消执行并返回
        List<Runnable> runnables = executorService.shutdownNow();
        //System.out.println(runnables);
        for (Runnable runnable : runnables) {
            System.out.println(runnable);
        }
    }
}

/**
 * 任务类 包含一个任务编号 在任务中 打印出是哪一个线程正在执行任务
 */
class MtRunnable4 implements Runnable {
    private int id;

    public MtRunnable4(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        //获取线程的名称 答应一句话
        String name = Thread.currentThread().getName();
        System.out.println(name+"执行了任务"+id);
    }

    @Override
    public String toString() {
        return "MtRunnable4{" +
                "id=" + id +
                '}';
    }
}
