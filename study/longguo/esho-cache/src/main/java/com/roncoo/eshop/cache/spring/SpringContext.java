package com.roncoo.eshop.cache.spring;

import org.springframework.context.ApplicationContext;

/**
 * @Author 李锦卓
 * @Description spring上下文
 * @Date 2022/7/18 23:29
 * @Version 1.0
 */
public class SpringContext {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringContext.applicationContext = applicationContext;
    }
}
