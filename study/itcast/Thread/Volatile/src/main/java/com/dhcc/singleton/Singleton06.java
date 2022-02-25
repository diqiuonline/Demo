package com.dhcc.singleton;

/**
 * 李锦卓
 * 2022/2/25 22:28
 * 1.0
 * 目标：懒汉式（线程安全 volatile双重检查模式 ）
 * 步骤
 * 1 构造器私有
 * 2 定义一个静态变量存储一个单例对象
 * 3 提供一个方法 进行双重检查机制 返回一个单例对象
 * 4 必须使用volatile修饰静态变量
 */
public class Singleton06 {
    //定义一个静态变量存储一个单例对象
    private volatile static Singleton06 INSTANCE;


    //构造器私有
    private Singleton06() {
    }

    //返回一个单例对象
    public static Singleton06 getInstance() {
        //判断单例对象的变量是否为空
        if (INSTANCE == null) {
            //很多线程执行到这里
            synchronized (Singleton06.class) {
                //第二次检查
                if (INSTANCE == null) {
                    INSTANCE = new Singleton06();
                }
            }
        }
        return INSTANCE;
    }
}
