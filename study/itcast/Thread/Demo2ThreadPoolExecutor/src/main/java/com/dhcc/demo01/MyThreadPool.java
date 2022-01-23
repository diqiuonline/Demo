package com.dhcc.demo01;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 这是自定义的线程池类
 * 成员变量
 * 1:任务队列 集合  需要控制线程安全问题
 * 2:当前线程数量
 * 3：核心线程数量
 * 4：最大线程数量
 * 5：最大队列长度
 * 成员方法
 * 1：提交任务
 * 将任务添加到集合中，需要判断是否超出了任务总长度
 * 2：执行任务
 * 判断当前线程数量，决定创建核心线程还是非核心线程
 */
public class MyThreadPool {
    //1:任务队列 集合  需要控制线程安全问题
    private List<Runnable> task = Collections.synchronizedList(new LinkedList<>());
    //2:当前线程数量
    private int num;
    //3：核心线程数量
    private int corePoolSize;
    //4：最大线程数量
    private int maxSize;
    //5：最大队列长度
    private int workSize;

    public MyThreadPool(int corePoolSize, int maxSize, int workSize) {
        this.corePoolSize = corePoolSize;
        this.maxSize = maxSize;
        this.workSize = workSize;
    }

    //1：提交任务
    public void submit(Runnable r) {
        //判断当前集合中任务的数量是否超出做大任务数量
        if (task.size() >= workSize) {
            System.out.println("任务：" + r + "被丢弃了。。。。。");
        } else {
            task.add(r);
            //执行任务
            execTask(r);
        }
    }
    //执行任务
    private void execTask(Runnable r) {
        //判断当前线程数量，决定创建核心线程还是非核心线程
        if (num < corePoolSize) {
            new MyWorker("核心线程" + num, task).start();
            num++;
        } else if (num < maxSize) {
            new MyWorker("非核心线程" + num, task).start();
            num++;
        } else {
            System.out.println("任务："+r+"被缓存起来了");
        }
    }

}
