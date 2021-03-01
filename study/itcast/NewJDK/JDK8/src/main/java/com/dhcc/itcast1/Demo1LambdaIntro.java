package com.dhcc.itcast1;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/9/9 20:22
 */
public class Demo1LambdaIntro {
    public static void main(String[] args) {
        //使用匿名内部类存在的问题
        new Thread(new Runnable() {
            /**
             * 匿名内部类做了那些方法
             * 1 定义了一个没有名字的类
             * 2 这个类实现了runable接口
             * 3 创建了这个类的对象
             * 4 其实我们最关注的是run方法和l里面要执行的代码
             *
             * 使用匿名内部类的语法是很冗余的
             * Lambda 体现的是函数式编程思想，只需要将执行的代码放到函数中（函数就是类中的方法）
             * Lambda 就是一个匿名函数 我们只需要将要执行的代码   放到lambda中即可
             */
            public void run() {
                System.out.println("新线程");
            }
        }).start();

        //体验lamdba表达式
        new Thread(() -> {
            System.out.println("第二个线程");
        }).start();
        //lambda表达式的好处 可以简化匿名内部类  让代码更加精简
    }
}
