package com.dhcc.singleton;

/**
 * 李锦卓
 * 2022/2/25 22:28
 * 1.0
 * 目标：懒汉式（线程不安全）
 * 步骤
 * 1 构造器私有
 * 2 定义一个静态变量存储一个单例对象
 * 3 提供一个方法 返回一个单例对象
 *  性能得到了优化 但是任然蹦年保证第一次获取对象的线程安全
 */
public class Singleton05 {
    //定义一个静态变量存储一个单例对象
    private static Singleton05 INSTANCE;


    //构造器私有
    private Singleton05() {
    }

    //返回一个单例对象
    public static Singleton05 getInstance() {
        //判断单例对象的变量是否为空
        if (INSTANCE == null) {
            //很多线程执行到这里
            synchronized (Singleton05.class) {
                INSTANCE = new Singleton05();
            }
        }
        return INSTANCE;
    }
}
