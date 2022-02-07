package com.dhcc.Demo04;

/**
 * 李锦卓
 * 2022/2/6 20:45
 * 1.0
 */
public class Demo01 {
    public static void main(String[] args) {
        synchronized (Demo01.class) {
            System.out.println("1");
        }
    }
    public synchronized void test() {
        System.out.println("a");
    }
}
