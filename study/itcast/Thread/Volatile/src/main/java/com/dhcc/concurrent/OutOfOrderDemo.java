package com.dhcc.concurrent;

/**
 * 李锦卓
 * 2022/2/14 22:51
 * 1.0
 *      目标 研究重排序情况下可能带来的问题
 */
public class OutOfOrderDemo {
    public volatile static int a = 0, b = 0;
    public volatile  static int i = 0, j = 0;

    public static void main(String[] args) throws InterruptedException {
        int count = 0;
        //线程a
        while (true) {
            count++;
            a = 0; b = 0; i = 0; j = 0;

            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    a = 1;
                    i = b;
                }
            });








            //线程b
            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    b = 1;
                    j = a;
                }
            });


            t1.start();
            t2.start();
            t1.join();
            t2.join();
            System.out.println("第"+count+"得到的结果"+a + b+i+j);
            if (i == 0 && j == 0) {
                break;
            }
        }

    }
}
