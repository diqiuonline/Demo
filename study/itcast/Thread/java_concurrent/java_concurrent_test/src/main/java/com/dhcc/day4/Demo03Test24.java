package com.dhcc.day4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 李锦卓
 * 2022/4/12 21:49
 * 1.0
 */
@Slf4j(topic = "c.Demo03Test24")
public class Demo03Test24 {
    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        //创建一个条件变量（休息室）
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();


        lock.lock();
        try {
            //进入休息室1
            condition1.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //叫醒一个
        condition1.signal();
        //叫醒全部
        condition1.signalAll();
    }
}
