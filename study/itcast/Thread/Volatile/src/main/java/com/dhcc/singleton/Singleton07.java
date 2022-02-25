package com.dhcc.singleton;

/**
 * 李锦卓
 * 2022/2/25 22:28
 * 1.0
 * 目标：基于类的初始化实现延迟加载和线程安全的单例设计
 * 步骤
 * 1 构造器私有
 * 2 提供一个静态内部类 里面提供一个常量存储一个单例对象
 * 3 提供一个方法返回静态内部类中的单例对象

 */
public class Singleton07 {



    //构造器私有
    private Singleton07() {
    }

    private static class Inner {
        private static final Singleton07 INSTANCE = new Singleton07();
    }
    //提供一个方法返回静态内部类中的单例对象
    public static Singleton07 getInstance() {
        return Inner.INSTANCE;
    }
}
