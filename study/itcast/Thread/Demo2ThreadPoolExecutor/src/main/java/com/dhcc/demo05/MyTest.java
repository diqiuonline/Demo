package com.dhcc.demo05;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 李锦卓
 * 2022/2/5 15:11
 * 1.0
 * 主程序类 测试任务类
 */
public class MyTest {
    public static void main(String[] args) {
        //创建一个线程池对象
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(3, 5, 1, TimeUnit.MICROSECONDS, new LinkedBlockingDeque<>(15));
        //循环创建任务对象
        for (int i = 1; i <= 20; i++) {
            MyTask myTask = new MyTask("客户"+i);
            poolExecutor.submit(myTask);
        }
        //关闭线程池
        poolExecutor.shutdown();
    }
}
