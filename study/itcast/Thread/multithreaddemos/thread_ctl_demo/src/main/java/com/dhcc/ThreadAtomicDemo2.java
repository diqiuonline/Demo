package com.dhcc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 李锦卓
 * 2022/3/8 20:40
 * 1.0
 */
public class ThreadAtomicDemo2 {
    private static AtomicInteger atomicInteger; //执行n++
    public static void main(String[] args) throws InterruptedException {
        for (int j = 0; j < 100; j++) {
            atomicInteger = new AtomicInteger(0);
            Thread thread1 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    atomicInteger.incrementAndGet();
                }
            });

            Thread thread2 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    atomicInteger.incrementAndGet();
                }
            });
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
            System.out.println(atomicInteger.get());
        }
    }
}
/**
 * 2000
 * 2000
 * 2000
 * 2000
 * 2000
 * 2000
 * 2000
 * 2000
 * 2000
 * 2000
 * 2000
 * 2000
 * 2000
 * 2000
 */