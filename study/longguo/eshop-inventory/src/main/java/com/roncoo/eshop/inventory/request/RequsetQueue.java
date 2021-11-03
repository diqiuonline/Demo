package com.roncoo.eshop.inventory.request;

import com.roncoo.eshop.inventory.thread.RequestProcessorThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 请求内存队列
 */
public class RequsetQueue {
    /**
     * 内存队列
     */
    private List<ArrayBlockingQueue<Request>> queues = new ArrayList<ArrayBlockingQueue<Request>>();
    /**
     * 单例有很多种实现方式  这里采取一种绝对线程安全的一种方式
     * 静态内部类的方式 去初始化单例
     */
    private static class Singleton{

        private static RequsetQueue instance;
        static {
            instance = new RequsetQueue();
        }
        public static RequsetQueue getInstance() {
            return instance;
        }

    }



    /**
     * jvm机制去保证多线程并发安全
     * 内部类的初始化 一定指挥发生一次，不管多少个线程去并发初始化
     * @return
     */
    public static RequsetQueue getInstance() {
        return Singleton.getInstance();
    }

    /**
     * 添加一个内存队列
     * @param queue
     */
    public void addQueue(ArrayBlockingQueue<Request> queue) {
        this.queues.add(queue);
    }


}
