package com.dhcc;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/7/26 13:48
 */
public interface inter {
    default void method1() {
        System.out.println("默认");
    }

    public static void say() {
        System.out.println("这是MyInterface1中的静态方法");
    }
}
