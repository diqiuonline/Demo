package com.dhcc.day8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j(topic = "c.Demo04TestCountDownLatch")
public class Demo04TestCountDownLatch {


    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        new Thread(()->{
            log.debug("begin");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            latch.countDown();
            log.debug("end....");
        }).start();

        new Thread(()->{
            log.debug("begin");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            latch.countDown();
            log.debug("end");
        }).start();

        new Thread(()->{
            log.debug("begin");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            latch.countDown();
            log.debug("end");
        }).start();
        log.debug("waiting");
        latch.await();
        log.debug("main begin");
    }
}
