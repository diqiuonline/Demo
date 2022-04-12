package com.dhcc.day4;

import lombok.extern.slf4j.Slf4j;

/**
 * 李锦卓
 * 2022/4/12 16:50
 * 1.0
 */
@Slf4j(topic = "c.Demo01TestDeadLock")
public class Demo01TestDeadLock {
    public static void main(String[] args) {
        Chopstick c1 = new Chopstick("1");
        Chopstick c2 = new Chopstick("2");
        Chopstick c3 = new Chopstick("3");
        Chopstick c4 = new Chopstick("4");
        Chopstick c5 = new Chopstick("5");
        new Philssopher("苏格拉底",c1,c2).start();
        new Philssopher("柏拉图",c2,c3).start();
        new Philssopher("亚里士多德",c3,c4).start();
        new Philssopher("哈剌克利塔",c4,c5).start();
        new Philssopher("阿基米德",c5,c1).start();
    }
}


@Slf4j(topic = "c.Philssopher")
class Philssopher extends Thread {
    Chopstick left;
    Chopstick right;

    public Philssopher(String name, Chopstick left, Chopstick right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true) {
            //尝试获得左手的筷子
            synchronized (left) {
                //尝试获得右手的筷子
                synchronized (right) {
                    eat();
                }
            }
        }
    }

    private void eat() {
        log.debug("eating.....");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}



class Chopstick{
    String name;

    public Chopstick(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "筷子{" +
                 name + '\'' +
                '}';
    }
}