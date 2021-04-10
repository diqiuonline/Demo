package com.enjoy.jack.InstantiationAwareBeanPostProcessor;

import com.enjoy.jack.bean.Jack;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @Classname InstantiationAwareBeanPostProcessorDemo
 * @Description TODO
 * @Author Jack
 * Date 2020/12/22 20:27
 * Version 1.0
 */
/*@PropertySource("classpath:application.properties")
@Component*/
public class InstantiationAwareBeanPostProcessorDemo implements InstantiationAwareBeanPostProcessor {

    @Autowired
    private Environment environment;

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if(bean instanceof Jack) {
            if(environment.getProperty("enjoy.name").equalsIgnoreCase("jack")) {
                return true;
            }
        }
        return false;
    }
}
