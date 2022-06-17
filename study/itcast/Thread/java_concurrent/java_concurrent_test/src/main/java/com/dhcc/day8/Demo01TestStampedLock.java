package com.dhcc.day8;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.concurrent.locks.StampedLock;

@Slf4j(topic = "c.Demo01TestStampedLock")
public class Demo01TestStampedLock {
    public static void main(String[] args) {
        DataContainerStamped dataContainer = new DataContainerStamped(1);
        new Thread(()->{
            int read = dataContainer.read(1000);
            //System.out.println(read);
        },"t1").start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread(()->{
            dataContainer.write(1000);
            //System.out.println(read);
        },"t2").start();
    }
}

@Slf4j(topic = "c.DataContainerStamped")
class DataContainerStamped{
    private int data;
    private final StampedLock lock = new StampedLock();

    public DataContainerStamped(int data) {
        this.data = data;
    }

    public int read(int readTime) {
        long stamp = lock.tryOptimisticRead();
        log.debug("optimistic read locking ... {}", stamp);
        try {
            Thread.sleep(readTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (lock.validate(stamp)) {
            log.debug("read finish .. {}", stamp);
            return data;
        }
        //锁升级
        log.debug("updating to read lock ... {}", stamp);
        try {
            stamp = lock.readLock();
            log.debug("read lock {}", stamp);
            Thread.sleep(readTime);
            log.debug("read finish ... {}", stamp);
            return data;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            log.debug("read unlock {}", stamp);
            lock.unlockRead(stamp);

        }
    }

    public void write(int newData) {
        long stamp = lock.writeLock();
        log.debug("write lock {}", stamp);

        try {
            Thread.sleep(2000);
            this.data = newData;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            log.debug("write unlock {}", stamp);
            lock.unlockWrite(stamp);
        }
    }
}