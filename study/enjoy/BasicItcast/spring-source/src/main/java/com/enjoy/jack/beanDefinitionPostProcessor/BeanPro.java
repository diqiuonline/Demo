package com.enjoy.jack.beanDefinitionPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @Classname BeanPro
 * @Description TODO
 * @Author Jack
 * Date 2020/12/8 21:17
 * Version 1.0
 */
@Component
public class BeanPro implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory beanFactory1 = (DefaultListableBeanFactory)beanFactory;
        beanFactory1.setAllowBeanDefinitionOverriding(true);
        beanFactory1.setAllowCircularReferences(true);
    }
}
