package com.roncoo.eshop.inventory.thread;

import com.roncoo.eshop.inventory.request.Request;
import com.roncoo.eshop.inventory.request.RequsetQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池单例
 * 请求处理线程池
 */
public class RequestProcessorThreadPool {
    /**
     * 线程池
     */
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public RequestProcessorThreadPool() {
        RequsetQueue requsetQueue = RequsetQueue.getInstance();
        for (int i = 0; i < 10; i++) {
            ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<Request>(100);
            requsetQueue.addQueue(queue);
            threadPool.submit(new RequestProcessorThread(queue));
        }
    }

    /**
     * 单例有很多种实现方式  这里采取一种绝对线程安全的一种方式
     * 静态内部类的方式 去初始化单例
     */
    private static class Singleton{

        private static RequestProcessorThreadPool instance;
        static {
            instance = new RequestProcessorThreadPool();
        }
        public static RequestProcessorThreadPool getInstance() {
            return instance;
        }

    }



    /**
     * jvm机制去保证多线程并发安全
     * 内部类的初始化 一定指挥发生一次，不管多少个线程去并发初始化
     * @return
     */
    public static RequestProcessorThreadPool getInstance() {
        return Singleton.getInstance();
    }
    /**
     * 初始化的 便捷方法
     */
    public static   void init() {
        getInstance();
    }


}
