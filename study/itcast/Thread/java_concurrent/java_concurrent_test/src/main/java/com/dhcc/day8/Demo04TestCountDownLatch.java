package com.dhcc.day8;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j(topic = "c.Demo04TestCountDownLatch")
public class Demo04TestCountDownLatch {


    public static void main(String[] args) throws InterruptedException {
        //test();
        //test2();
        ExecutorService service = Executors.newFixedThreadPool(10);
        String[] all = new String[10];
        Random r = new Random();
        CountDownLatch latch = new CountDownLatch(10);
        for (int j = 0; j < 10; j++) {
            int k = j;
            service.submit(()->{

                for (int i = 0; i <= 100; i++) {
                    try {
                        Thread.sleep(r.nextInt(100));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    all[k] = i + "%";
                    System.out.print("\r"+Arrays.toString(all));

                }
                latch.countDown();
            });
        }
        latch.await();
        System.out.println("\n游戏开始");
        service.shutdown();
    }

    private static void test2() {
        CountDownLatch latch = new CountDownLatch(3);
        ExecutorService service = Executors.newFixedThreadPool(4);
        service.submit(()->{
            log.debug("begin");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            latch.countDown();
            log.debug("end....");
        });

        service.submit(()->{
            log.debug("begin");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            latch.countDown();
            log.debug("end....");
        });

        service.submit(()->{
            log.debug("begin");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            latch.countDown();
            log.debug("end....");
        });

        service.submit(()->{
            try {
                log.debug("waiting");
                latch.await();
                log.debug("main begin");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void test() throws InterruptedException {
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
