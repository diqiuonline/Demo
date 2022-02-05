package com.dhcc.Demo06;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 李锦卓
 * 2022/2/5 15:26
 * 1.0
 */
public class MyTest {
    public static void main(String[] args) {
        //创建线程池对象
        ExecutorService pool = Executors.newFixedThreadPool(2, new ThreadFactory() {
            int id = 1;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "ATM" + id++);
            }
        });
        // 创建2个任务并提交
        for (int i = 0; i < 2; i++) {
            MyTask myTask = new MyTask("客户" + i, 800);
            pool.submit(myTask);
        }
        // 关闭线程池
        pool.shutdown();
    }
}
