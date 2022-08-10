package com.spring;

/**
 * @Author 李锦卓
 * @Description TODO
 * @Date 2022/8/10 16:11
 * @Version 1.0
 */
public interface BeanPostProcessor {
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
