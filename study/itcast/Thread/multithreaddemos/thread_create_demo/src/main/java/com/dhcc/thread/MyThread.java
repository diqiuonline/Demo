package com.dhcc.thread;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * 李锦卓
 * 2022/2/26 11:41
 * 1.0
 */
public class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("MyThread 执行了"+new Date().getTime());
        }
    }
}
