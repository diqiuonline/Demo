package com.dhcc;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 李锦卓
 * 2022/3/8 20:40
 * 1.0
 */
public class ThreadAtomicDemo3 {
    private static AtomicStampedReference<Integer> atomicInteger; //执行n++
    public static void main(String[] args) throws InterruptedException {
        for (int j = 0; j < 100; j++) {
            atomicInteger = new AtomicStampedReference<>(0,0);
            Thread thread1 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    int stamp ;
                    Integer reference;
                    do {
                        stamp = atomicInteger.getStamp();
                        reference = atomicInteger.getReference();
                    } while (!atomicInteger.compareAndSet(reference, reference+1,stamp,stamp+1));
                }
            });

            Thread thread2 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    int stamp ;
                    Integer reference;
                    do {
                        stamp = atomicInteger.getStamp();
                        reference = atomicInteger.getReference();
                    } while (!atomicInteger.compareAndSet(reference, reference+1,stamp,stamp+1));

                }
            });
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
            System.out.println(atomicInteger.getReference());
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