package com.dhcc.day5;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 李锦卓
 * 2022/5/9 21:27
 * 1.0
 */
@Slf4j(topic = "c.Demo05Test41")
public class Demo06Test41 {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            demo(() -> new AtomicLong(0),(adder) -> adder.getAndIncrement() );
            demo(()->new LongAdder(),adder -> adder.increment());
        }
    }

    /**
     *
     * @param addreSuplier ()->结果 提供一个累加器对象
     * @param action （参数）->  没有返回结果 执行累加操作
     * @param <T>
     */
    private static <T> void demo(Supplier<T> addreSuplier, Consumer<T> action) {
        T adder = addreSuplier.get();
        List<Thread> ts = new ArrayList<>();
        //四个线程 每人累加50w
        for (int i = 0; i < 4; i++) {
            ts.add(new Thread(()->{
                for (int j = 0; j < 500000; j++) {
                    action.accept(adder);
                }
            }));
        }
        long start = System.nanoTime();
        ts.forEach(t->t.start());
        ts.forEach(t-> {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        long end = System.nanoTime();
        System.out.println(adder+"cost:"+(end-start));
    }
}
