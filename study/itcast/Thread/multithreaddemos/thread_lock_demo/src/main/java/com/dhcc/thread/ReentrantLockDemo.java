package com.dhcc.thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 李锦卓
 * 2022/3/9 20:35
 * 1.0
 */
public class ReentrantLockDemo {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        for (int i = 0; i < 10; i++) {
            lock.lock();
            System.out.println("加锁次数" + (i + 1));
        }
        for (int i = 0; i < 10; i++) {
            try {

                System.out.println("解锁次数" + (i + 1));
            }finally {
                lock.unlock();
            }
        }
    }
}
/**
 * 加锁次数1
 * 加锁次数2
 * 加锁次数3
 * 加锁次数4
 * 加锁次数5
 * 加锁次数6
 * 加锁次数7
 * 加锁次数8
 * 加锁次数9
 * 加锁次数10
 * 解锁次数1
 * 解锁次数2
 * 解锁次数3
 * 解锁次数4
 * 解锁次数5
 * 解锁次数6
 * 解锁次数7
 * 解锁次数8
 * 解锁次数9
 * 解锁次数10
 */