package com.dhcc.day5;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 李锦卓
 * 2022/4/28 20:21
 * 1.0
 */
@Slf4j(topic = "c.Demo04Test39")
public class Demo04Test39 {
    public static void main(String[] args) {
        /*demo(() -> new int[2]
                , array -> array.length
                , (array, index) -> array[index]++
                , array -> System.out.println(Arrays.toString(array))
        );*/

        demo(()->new AtomicIntegerArray(2)
                , array -> array.length()
                , (array, index) -> array.getAndIncrement(index)
                , array -> System.out.println(array)
        );
    }

    /**
     *
     * @param arraySupplier 提供数据 可以是线程安全的 也可以是线程不安全的
     * @param lengthFun 获取数组长度的方法
     * @param putConsumer 自增方法，回传array index
     * @param printConsumer 打印数组的方法
     * @param <T>
     *     supplier 提供者 无中生有 （）->结果
     *     function 函数 一个参数一个结果 （参数）->结果， bifunction (参数1，参数2） -> 结果
     *     consumer 消费者 一个参数没有结果 （参数）-> void biConsumer (参数1，参数2) ->
     */
    private static <T> void demo(
            Supplier<T> arraySupplier,
            Function<T, Integer> lengthFun,
            BiConsumer<T, Integer> putConsumer,
            Consumer<T> printConsumer
            ) {
        List<Thread> ts = new ArrayList<>();
        T array = arraySupplier.get();
        Integer length = lengthFun.apply(array);
        for (Integer i = 0; i < length; i++) {
            ts.add(new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    putConsumer.accept(array, j % length);
                    //t1 【0,0】 0-0 --[1,0]
                    //t1 【1,0】 1-1 --[1,1]
                    //t2 【1,1】 2-0 --[2,1]
                    //t1  [2,1] 3-1 --[2,2]
                }
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
        printConsumer.accept(array);
    }
}



