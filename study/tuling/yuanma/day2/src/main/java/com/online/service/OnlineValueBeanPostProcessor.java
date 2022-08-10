package com.online.service;

import com.spring.BeanPostProcessor;
import com.spring.Component;

import java.lang.reflect.Field;

/**
 * @Author 李锦卓
 * @Description TODO
 * @Date 2022/8/10 16:29
 * @Version 1.0
 */
@Component
public class OnlineValueBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(OnlineValue.class)) {
                field.setAccessible(true);
                try {
                    field.set(bean,field.getAnnotation(OnlineValue.class).value());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bean;
    }
}
