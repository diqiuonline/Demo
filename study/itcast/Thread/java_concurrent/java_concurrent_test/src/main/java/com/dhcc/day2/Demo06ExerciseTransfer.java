package com.dhcc.day2;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * 李锦卓
 * 2022/4/6 22:33
 * 1.0
 */
@Slf4j(topic = "c.Demo06ExerciseTransfer")
public class Demo06ExerciseTransfer {
    public static void main(String[] args) throws InterruptedException {
        Account a = new Account(1000);
        Account b = new Account(1000);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                a.transfer(b, randomAmount());
            }}, "t1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                b.transfer(a, randomAmount());
            }}, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.debug("total:{}",a.getMoney()+b.getMoney());
    }


    //random为线程安全
    static Random random = new Random();
    //随机1-5
    public static int randomAmount(){
        return random.nextInt(100) + 1;
    }
}

//账户
class Account{
    private Object object = new Object();
    private int money;

    public Account(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    //转账
    public void transfer(Account target, int amount) {
        synchronized (Account.class) {
            if (this.money >= amount) {
                setMoney(getMoney() - amount);
                target.setMoney(target.getMoney() + amount);
            }
        }
    }
}
