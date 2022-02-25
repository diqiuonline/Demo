package com.dhcc.uservolatile;

/**
 * 李锦卓
 * 2022/2/25 23:09
 * 1.0
 * 目标 volatile的使用场景二 可以作为刷新之前操作的触发器
 */
public class UserVolatile02 {
    int a = 1;
    int b = 2;
    int c = 3;
    volatile boolean flag = false;

    public void write() {
        a = 100;
        b = 200;
        c = 300;
        flag = true;
    }
    public void read() {
        while (flag) {
            System.out.println("a = " + a + ", b = " + b + ", c = " + c);
        }
    }

    public static void main(String[] args) {
        UserVolatile02 target = new UserVolatile02();
        new Thread(() ->{
            target.write();
        }).start();


        new Thread(() ->{
            target.read();
        }).start();
    }
}
