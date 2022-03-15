package com.dhcc.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 李锦卓
 * 2022/3/9 20:42
 * 1.0
 */
public class ReadWriteLockDemo {
    private Map<String, String> map = new HashMap<>(); //操作的map对象
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock(); //读操作锁
    private ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock(); //写操作锁

    public String get(String key) {
        readLock.lock(); //读操作加锁
        try {
            System.out.println(Thread.currentThread().getName()+"读操作已加锁 开始读操作 。。。。。");
            Thread.sleep(3000);
            return map.get(key);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            readLock.unlock();
            System.out.println(Thread.currentThread().getName()+"读操作已解锁 读操作结束 。。。。。");
        }
    }


    public void put(String key,String value) {
        writeLock.lock(); //读操作加锁
        try {
            System.out.println(Thread.currentThread().getName()+"写操作已加锁 正在写操作 。。。。。");
            Thread.sleep(3000);
            map.put(key,value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            writeLock.unlock();
            System.out.println(Thread.currentThread().getName()+"写操作已解锁 写操作完成 。。。。。");
        }
    }

    public static void main(String[] args) {
        ReadWriteLockDemo readWriteLockDemo = new ReadWriteLockDemo();
        readWriteLockDemo.put("key1", "value1");
        new Thread("读线程1"){
            public void run() {
                System.out.println(readWriteLockDemo.get("key1"));
            }
        }.start();

        new Thread("读线程2"){
            public void run() {
                System.out.println(readWriteLockDemo.get("key1"));
            }
        }.start();

        new Thread("读线程3"){
            public void run() {
                System.out.println(readWriteLockDemo.get("key1"));
            }
        }.start();
    }
}
/**
 * main写操作已加锁 正在写操作 。。。。。
 * main写操作已解锁 写操作完成 。。。。。
 * 读线程1读操作已加锁 开始读操作 。。。。。
 * 读线程2读操作已加锁 开始读操作 。。。。。
 * 读线程3读操作已加锁 开始读操作 。。。。。
 * 读线程1读操作已解锁 读操作结束 。。。。。
 * 读线程3读操作已解锁 读操作结束 。。。。。
 * 读线程2读操作已解锁 读操作结束 。。。。。
 * value1
 * value1
 * value1
 */