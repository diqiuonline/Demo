package com.dhcc;

/**
 * 李锦卓
 * 2022/3/8 20:40
 * 1.0
 */
public class ThreadAtomicDemo {
    private static int n; //执行n++
    public static void main(String[] args) throws InterruptedException {
        for (int j = 0; j < 100; j++) {
            n = 0;
            Thread thread1 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    n++;
                }
            });

            Thread thread2 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    n++;
                }
            });
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
            System.out.println(n);
        }
    }
}
/**
 * 2000
 * 1186
 * 2000
 * 2000
 * 2000
 * 2000
 * 2000
 * 1396
 * 2000
 */