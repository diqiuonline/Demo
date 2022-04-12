package com.dhcc.day4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 李锦卓
 * 2022/4/12 16:50
 * 1.0
 */
@Slf4j(topic = "c.Demo02TestDeadLock")
public class Demo02TestDeadLock {
    public static void main(String[] args) {
        Chopstick2 c1 = new Chopstick2("1");
        Chopstick2 c2 = new Chopstick2("2");
        Chopstick2 c3 = new Chopstick2("3");
        Chopstick2 c4 = new Chopstick2("4");
        Chopstick2 c5 = new Chopstick2("5");
        new Philssopher2("苏格拉底",c1,c2).start();
        new Philssopher2("柏拉图",c2,c3).start();
        new Philssopher2("亚里士多德",c3,c4).start();
        new Philssopher2("哈剌克利塔",c4,c5).start();
        new Philssopher2("阿基米德",c5,c1).start();
    }
}


@Slf4j(topic = "c.Philssopher")
class Philssopher2 extends Thread {
    Chopstick2 left;
    Chopstick2 right;

    public Philssopher2(String name, Chopstick2 left, Chopstick2 right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true) {
            //尝试获得左手的筷子
            if (left.tryLock()) {
                try {
                    //尝试获得右手的筷子
                    if (right.tryLock()) {
                        try {
                            eat();
                        }finally {
                            right.unlock();
                        }
                    }
                }finally {
                    left.unlock();
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



class Chopstick2 extends ReentrantLock {
    String name;

    public Chopstick2(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "筷子{" +
                 name + '\'' +
                '}';
    }
}