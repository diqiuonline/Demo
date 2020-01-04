package com.donghua.test;

/**
 * User: 李锦卓
 * Time: 2018/12/12 10:38
 */
public class Two extends One {
    public static void main(String[] args) {
        Two t = new Two();
        t.printAB();
    }
    protected void printA() {
        System.out.println("two A");
    }
    private void printB() {
        System.out.println("two B");
    }


}