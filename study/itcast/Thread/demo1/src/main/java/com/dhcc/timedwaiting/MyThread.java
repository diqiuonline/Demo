package com.dhcc.timedwaiting;

/**
 * 限时等待
 */
public class MyThread extends Thread{
    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        myThread.start();
        for (int i = 0; i < 10; i++) {

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("    mt的线程状态"+myThread.getState());

        }
    }
        //实现一个计数器，计数从1到100 在每个数字中间暂停1s 每隔10个数字输出一个字符串
    @Override
    public void run() {
        for (int i = 0; i < 99; i++) {
            if (i % 10 == 0) {
                System.out.println("------------达到了输出要求"+i);
            }
            System.out.print(i);
            try {
                sleep(1000);
                System.out.println("             休息一秒钟");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
