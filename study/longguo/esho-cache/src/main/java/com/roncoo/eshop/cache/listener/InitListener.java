package com.roncoo.eshop.cache.listener;

import com.roncoo.eshop.cache.kafka.KafkaConsumer;
import com.roncoo.eshop.cache.spring.SpringContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @Author 李锦卓
 * @Description 系统初始化监听器
 * @Date 2022/7/17 20:21
 * @Version 1.0
 */
public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        ApplicationContext c = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        SpringContext.setApplicationContext(c);

        new Thread(new KafkaConsumer("cache-message")).start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
