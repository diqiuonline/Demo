package com.dhcc.singleton;

/**
 * 李锦卓
 * 2022/2/25 21:44
 * 1.0
 * 饿汉式（静态代码块）
 * 步骤
 *  构造器私有
 *  定义一个静态常量保存一个唯一的实例对象（单例） 可以通过静态代码块初始化
 */
public class Singleton02 {
    //2 定义一个静态常量保存一个唯一的实例对象
    public static final Singleton02  INSTANCE;
    static {
        INSTANCE = new Singleton02();
    }


    //1 构造器私有
    private Singleton02() {

    }

    //提供一个方法返回单例对象
    public static Singleton02 getInstance() {
        return INSTANCE;
    }

}


class Test02 {
    public static void main(String[] args) {
        Singleton02 s1 = Singleton02.getInstance();
        Singleton02 s2 = Singleton02.getInstance();
        System.out.println(s1 == s2);
    }
}