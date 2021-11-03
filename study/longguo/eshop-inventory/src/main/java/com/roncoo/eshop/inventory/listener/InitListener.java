package com.roncoo.eshop.inventory.listener;

import com.roncoo.eshop.inventory.thread.RequestProcessorThreadPool;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 系统初始化监听器
 */
public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("=============系统初始化=====================");
        System.out.println("初始化工作线程池和内存队列");
        //RequestProcessorThreadPool requestProcessorThreadPool = RequestProcessorThreadPool.getInstance();
        RequestProcessorThreadPool.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
