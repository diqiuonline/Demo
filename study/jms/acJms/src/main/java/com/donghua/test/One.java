package com.donghua.test;

/**
 * User: 李锦卓
 * Time: 2018/12/12 10:38
 */
public class One {
    protected void printA() {
        System.out.println("OneA");
    }
    private void printB() {
        System.out.println("One B");
    }
    protected void printAB() {
        printA();
        printB();
    }
}
