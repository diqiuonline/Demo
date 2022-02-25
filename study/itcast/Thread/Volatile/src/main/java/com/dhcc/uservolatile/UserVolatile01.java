package com.dhcc.uservolatile;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 李锦卓
 * 2022/2/25 22:57
 * 1.0
 * 目标 volatile的使用场景
 * 1 适合做纯赋值操作 不适合做a++操作
 */
public class UserVolatile01 implements Runnable{
    //定义一个volatile修饰的boolean变量
    public volatile boolean flag = false;
    //定已一个原子类 记录总的赋值次数
    public AtomicInteger atomicInteger = new AtomicInteger(); //+1
    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            switchFlag();
            atomicInteger.incrementAndGet();
        }
    }

    private void switchFlag() {
        //flag = ture;
        // true
        //20000
        flag = !flag;
        //true
        //20000


        //flase
        //20000
    }
}

class Test{
    public static void main(String[] args) throws Exception {
        UserVolatile01 target = new UserVolatile01();
        // 创建两个线程对象
        Thread t1 = new Thread(target);
        Thread t2 = new Thread(target);

        t1.start();
        t2.start();


        //等两个线程执行完毕 执行获取结果
        t1.join();
        t2.join();

        System.out.println(target.flag);
        System.out.println(target.atomicInteger.get());
    }
}