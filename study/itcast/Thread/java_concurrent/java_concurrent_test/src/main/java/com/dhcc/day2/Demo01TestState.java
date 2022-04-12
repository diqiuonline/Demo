package com.dhcc.day2;

import lombok.extern.slf4j.Slf4j;

/**
 * 李锦卓
 * 2022/3/24 22:33
 * 1.0
 */
@Slf4j(topic = "c.Demo01TestState")
public class Demo01TestState {
    public static void main(String[] args) {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("running");
            }
        };

        Thread t2 = new Thread("t2") {
            @Override
            public void run() {
                while (true) {

                }
            }

        };
        t2.start();

        Thread t3 = new Thread("t3") {
            @Override
            public void run() {
                log.debug("running");
            }

        };
        t3.start();

        Thread t4 = new Thread("t4") {
            @Override
            public void run() {
                synchronized (Demo01TestState.class) {
                    try {
                        Thread.sleep(100000); //timed_waiting
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        };
        t4.start();

        Thread t5 = new Thread("t5") {
            @Override
            public void run() {
                try {
                    t2.join();  //waiting
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
        t5.start();

        Thread t6 = new Thread("t6") {
            @Override
            public void run() {
                synchronized (Demo01TestState.class) {
                    try {
                        Thread.sleep(100000); //BLOCKED
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        };
        t6.start();


        log.debug("t1 state {}",t1.getState());
        log.debug("t2 state {}",t2.getState());
        log.debug("t3 state {}",t3.getState());
        log.debug("t4 state {}",t4.getState());
        log.debug("t5 state {}",t5.getState());
        log.debug("t6 state {}",t6.getState());
    }
}
/**
 * 19:07:38.044 [main] c.Demo01TestState - t1 state NEW
 * 19:07:38.044 [t3] c.Demo01TestState - running
 * 19:07:38.047 [main] c.Demo01TestState - t2 state RUNNABLE
 * 19:07:38.047 [main] c.Demo01TestState - t3 state TERMINATED
 * 19:07:38.047 [main] c.Demo01TestState - t4 state TIMED_WAITING
 * 19:07:38.047 [main] c.Demo01TestState - t5 state WAITING
 * 19:07:38.047 [main] c.Demo01TestState - t6 state BLOCKED
 */