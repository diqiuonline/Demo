package com.dhcc.util;

import com.dhcc.controller.OrderController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test implements Runnable{

    static SynchronizedByKey synchronizedByKey = new SynchronizedByKey();
    @Override
    public void run() {

        synchronizedByKey.exec(String.valueOf(1),()->{
            System.out.println("开始");

            System.out.println("结束");

        });
    }
    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            new Thread(new Test()).start();
        }
    }


}
