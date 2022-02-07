package com.dhcc.demo01_concurrent_problem;

/**
 * 李锦卓
 * 2022/2/5 16:40
 * 1.0
 * 演示可见性问题
 * 1 创建一个共享变量
 * 2 创建一条线程不断读取共享变量
 * 3 创建一条线程不断读取共享变量
 */
public class Test01Visibility {
    private static boolean flag = true;

    public static void main(String[] args) throws InterruptedException {
        //创建一条线程不断读取共享变量
        new Thread(() -> {
            while (flag) {

            }
        }).start();
        Thread.sleep(2000);
        //创建一条线程不断读取共享变量
        new  Thread(()->{
            flag = false;
            System.out.println("线程修改了变量的只为false");
        }).start();
    }
}
