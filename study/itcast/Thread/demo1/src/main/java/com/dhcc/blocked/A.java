package com.dhcc.blocked;

public class A extends Thread{
    private  Object obj;
    public A(String name,Object obj ) {
        super(name);
        this.obj = obj;
    }
    @Override
    public void run() {
        synchronized (obj){
            System.out.println("线程A开始执行");
            System.out.println("线程A真正开始执行代码了");
            long beginTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - beginTime < 5000) {
                //模拟5秒钟的任务
            }
            System.out.println("线程A执行完毕");
        }

    }
}
