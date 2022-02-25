package com.dhcc.singleton;

/**
 * 李锦卓
 * 2022/2/25 21:54
 * 1.0
        目标 懒汉式（线程安全写法）
        步骤：
            1 构造器私有
            2 定义一个静态的变量存储一个单例对象 （定义的时候不初始化改对象）
            3 定义一个获取单例的方法 每次返回单例对象的时候先询问是否有对象 有直接返回，没有就创建一个单例对象
            4 为获取单例的方法加锁
 */

public class Singleton04 {
    //定义一个静态的变量存储一个单例对象（定义的时候不初始化该对象）
    private static Singleton04 INSTANCE;
    //构造器私有
    private Singleton04() {

    }

    //定义一个获取单例的方法 每次返回单例对象的时候先询问是否有对象 有直接返回，没有就创建一个单例对象
    // 加锁 线程安全
    public synchronized static Singleton04 getInstance() {
        if (INSTANCE == null) {
            //说明第一次来拿单例对象 需要真正的创建从胡来
            INSTANCE = new Singleton04();
        }
        return INSTANCE;
    }

}

class Test04 {

}