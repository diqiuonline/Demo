package com.enjoy.jack.SmartInstantiationAwareBeanPostProcessor;

import com.enjoy.jack.bean.circular.CircularRefA;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;


@Component
public class SmartInstantiationAwareBeanPostProcessorDemo implements SmartInstantiationAwareBeanPostProcessor {
    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        /*CircularRefA circularRefA = new CircularRefA();*/
        return bean;
    }
}
