package com.dhcc.itcast1;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/9/9 20:45
 */
public class Demo2LambdaIntro {
    /**
     * lambda的标准格式
     *      lambda是一个 匿名函数，而函数相当于Java中的方法
     *      （参数） -> {方法体}
     * @param args
     *
     *
     *
     * 匿名内部类在编译后回形成一个单独的class文件 带$符号
     * lambda在程序运行的时候会形成一个类
     *      在类中新增一个静态方法 这个方法的方法体就是lambda表达式中的方法
     *      还会形成一个匿名内部类 实现接口 重写抽象方法
     *      在重写的方法里面 调用我们新生成的静态方法
     */
    public static void main(String[] args) {
        goSwimming(new Swimmable() {
            @Override
            public void swimming() {
                System.out.println("我是匿名内部类的游泳");
            }
        });
        //以后 看到方法的参数是接口 就可以考虑使用Lambda表达式
        //可以这么任务 Lambda表达式 就是对接口中的抽象方法的重写
        goSwimming(() -> {
            System.out.println("我是lambda的游泳");
        });
        System.out.println("-------------------------------------------------");
        goSmoking(new Smokeable() {
            @Override
            public int smoking(String name) {
                System.out.println("匿名内部类"+name);
                return 0;
            }
        });


        goSmoking((String name) -> {
            System.out.println("lambda"+name);
            return 0;
        });


    }

    //练习一个无参数无返回值的lambda表达式
    public static void goSwimming(Swimmable s) {
        s.swimming();
    }
    //联系有参数 有返回值的Lambda
    public static void goSmoking(Smokeable s) {
        int smoking = s.smoking("中华");
        System.out.println(smoking);
    }

}
