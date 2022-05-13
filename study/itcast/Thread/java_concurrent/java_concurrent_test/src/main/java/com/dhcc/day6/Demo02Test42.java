package com.dhcc.day6;

import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 李锦卓
 * 2022/5/12 18:50
 * 1.0
 */
@Slf4j(topic = "c.Demo02Test42")
public class Demo02Test42 {
    public static void main(String[] args) {
        Account.demo(new MyAtomicInteger(100000));

    }

}


class MyAtomicInteger implements  Account{
    private volatile int value;
    private static final long valueOffset;
    static final Unsafe UNSAFE;
    static {
        UNSAFE = UnsafeAccessor.getUnsafe();
        try {
            valueOffset = UNSAFE.objectFieldOffset(MyAtomicInteger.class.getDeclaredField("value"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public MyAtomicInteger(int i) {
        this.value = i;
    }


    public int getValue() {
        return value;
    }

    public void decrement(int amount) {
        while (true) {
            int prev = this.value;
            int next = prev - amount;
            if (UNSAFE.compareAndSwapInt(this, valueOffset, prev, next)) {
                break;
            }
        }
    }

    @Override
    public int getBalance() {
        return getValue();
    }

    @Override
    public void withdraw(int amount) {
        decrement(amount);
    }
}


class UnsafeAccessor{
    private static final Unsafe unsafe;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static Unsafe getUnsafe(){
        return unsafe;
    }
}


interface Account{
    //获取余额
    int getBalance();

    //取款
    void withdraw(int amount);

    /**
     * 方法内启动1000个线程，每个线程做-10元的操作
     * 如果初始余额为1000 那么正确结果应当是0
     */
    static void demo(Account account) {
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(10);
            }));
        }
        ts.forEach(Thread::start);
        ts.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(account.getBalance());
    }
}