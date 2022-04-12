package com.dhcc.day1;

import lombok.extern.slf4j.Slf4j;

/**
 * 李锦卓
 * 2022/3/22 15:37
 * 1.0
 */
@Slf4j(topic = "c.Test9")
public class Test9 {
    public static void main(String[] args) {
        Runnable task1 = () ->{
            int count = 0;
            for (; ; ) {
                System.out.println("--------->1 "+count++);
            }
        };
        Runnable task2 = () ->{
            int count = 0;
            for (; ; ) {
                //Thread.yield();
                /**
                 * Thread.yield();
                 *
                 * --------->1 126580
                 *             --------->2 3637
                 * --------->1 126581
                 * --------->1 126582
                 * --------->1 126583
                 */
                System.out.println("            --------->2 "+count++);
            }
        };

        Thread t1 = new Thread(task1, "t1");
        Thread t2 = new Thread(task2, "t2");
        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.MAX_PRIORITY);
        /**
         * --------->1 11758
         *             --------->2 31648
         *             --------->2 31649
         *             --------->2 31650
         *             --------->2 31651
         *             --------->2 31652
         *             --------->2 31653
         *             --------->2 31654
         *             --------->2 31655
         *             --------->2 31656
         *             --------->2 31657
         *             --------->2 31658
         * --------->1 11759
         * --------->1 11760
         * --------->1 11761
         *             --------->2 31659
         */
        t1.start();
        t2.start();
    }
}
