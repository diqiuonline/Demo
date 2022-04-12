package com.dhcc.day2;

import java.util.ArrayList;

/**
 * 李锦卓
 * 2022/4/5 22:58
 * 1.0
 */
public class Demo04Test18 {
    static final int THREAD_NUMBER = 200;
    static final int LOOP_NUMBER = 200;

    public static void main(String[] args) {
        ThreadSafeSubClass test = new ThreadSafeSubClass();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(()->{
                test.method1(LOOP_NUMBER);
            },"Thread"+(i+1)).start();
        }
    }
}
class ThreadSafe{
    public final void method1(int loop_number) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < loop_number; i++) {
            method2(list);
            method3(list);
        }
    }
    public void method2(ArrayList<String> list) {
        list.add("1");
    }

    public void method3(ArrayList<String> list) {
        list.remove(0);

    }


}

class ThreadSafeSubClass extends ThreadSafe {
    @Override
    public void method3(ArrayList<String> list) {
        new Thread( ()->{
            list.remove(0);
        }).start();
    }
}

