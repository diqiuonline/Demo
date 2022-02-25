package com.dhcc.singleton;

/**
 * 李锦卓
 * 2022/2/25 21:44
 * 1.0
 * 饿汉式（静态常量）
 * 步骤
 * 构造器私有
 * 定义一个静态常量保存唯一的实例对象
 */
public class Singleton01 {
    //2 定义一个静态常量保存一个唯一的实例对象
    public static final Singleton01 INSTANCE = new Singleton01();
    //1 构造器私有
    private Singleton01() {

    }

    //提供一个方法返回单例对象
    public static Singleton01 getInstance() {
        return INSTANCE;
    }

}


class Test01 {
    public static void main(String[] args) {
        Singleton01 s1 = Singleton01.getInstance();
        Singleton01 s2 = Singleton01.getInstance();
        System.out.println(s1 +"      "+s2);
    }
}