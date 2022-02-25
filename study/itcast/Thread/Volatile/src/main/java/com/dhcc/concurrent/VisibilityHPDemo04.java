package com.dhcc.concurrent;

/**
 * 李锦卓
 * 2022/2/24 22:54
 * 1.0
 *
 *      目标 voliatile 写读建立的happens-before关系研究
 *      总规则：
 *          如果线程1写入了volatile变量v （灵界资源）
 *          接着线程2读取了v 那么 线程1写入v及之前的写操作都对线程2可见（线程1 线程2 可以是同一个线程）
 */
public class VisibilityHPDemo04 {
    private int a = 1;
    private int abc = 1;
    private volatile int b = 2;
    public void write() {
        a = 3;
        abc = 100;
        b = a;
    }
    public void read() {
        System.out.println("b=" + b + ", a=" + a+", abc = "+abc);
    }

    public static void main(String[] args) {
        while (true) {
            VisibilityHPDemo04 v = new VisibilityHPDemo04();

            //提供两个线程
            new Thread(() -> {
                v.write();
            }).start();

            new Thread(() -> {
                v.read();
            }).start();
        }


    }
}

/**
 *
 * b = 3 a = 3
 * b = 2 a = 1
 * b = 2 a == 3
 *  有没有可能 b = 3 a = 1 是有可能的 但是只要将volatile修饰了b之后 就不会出现这种情况
 *
 *
 */