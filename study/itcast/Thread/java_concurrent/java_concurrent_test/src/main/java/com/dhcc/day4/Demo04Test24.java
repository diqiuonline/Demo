package com.dhcc.day4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 李锦卓
 * 2022/4/12 22:00
 * 1.0
 */
@Slf4j(topic = "c.Demo04Test24")
public class Demo04Test24 {
    static final Object room = new Object();
    static boolean hasCigarette = false;
    static boolean hasTakeout = false;
    static ReentrantLock ROOM = new ReentrantLock();
    //等待烟的休息室
    static Condition waitCigaretteSet = ROOM.newCondition();
    //等待早餐的休息室
    static Condition waitTackSet = ROOM.newCondition();

    public static void main(String[] args) {
        new Thread(()->{
            ROOM.lock();
            try {
                log.debug("有烟没? {}", hasCigarette);
                while (!hasCigarette) {
                    log.debug("没烟 先歇会！");
                    try {
                        waitCigaretteSet.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                log.debug("可以开始干活了");

            }finally {
                ROOM.unlock();
            }


        },"小南").start();


        new Thread(()->{
            ROOM.lock();
            try {
                log.debug("外卖送到没? {}", hasTakeout);
                while (!hasTakeout) {
                    log.debug("没外卖 先歇会！");
                    try {
                        waitTackSet.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                log.debug("可以开始干活了");

            }finally {
                ROOM.unlock();
            }


        },"小女").start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(()->{
            ROOM.lock();
            try {
                waitTackSet.signal();
                hasTakeout = true;
            }finally {
                ROOM.unlock();
            }
        },"送外卖的").start();


        new Thread(()->{
            ROOM.lock();
            try {
                waitCigaretteSet.signal();
                hasCigarette = true;
            }finally {
                ROOM.unlock();
            }
        },"送烟的").start();
    }
}
