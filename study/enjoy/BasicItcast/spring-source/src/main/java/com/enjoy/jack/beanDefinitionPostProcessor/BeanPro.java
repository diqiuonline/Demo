package com.enjoy.jack.beanDefinitionPostProcessor;

import com.enjoy.jack.annotation.MyService;
import com.enjoy.jack.bean.BeanDefinitionBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;


//@Component
public class BeanPro implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        final String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanDefinitionName);
            //registry.removeBeanDefinition("james13");

            //System.out.println(beanDefinition);
        }
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setBeanClass(BeanDefinitionBean.class);
        MutablePropertyValues propertyValues = genericBeanDefinition.getPropertyValues();
        propertyValues.add("name", "jack");
        registry.registerBeanDefinition("beanDefinitionBean", genericBeanDefinition);
        //System.out.println(registry);


        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        //需要过滤的注解
        /*scanner.addIncludeFilter(new AnnotationTypeFilter(MyService.class));
        scanner.scan("com.enjoy.jack.customBean");*/
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory beanFactory1 = (DefaultListableBeanFactory)beanFactory;
        beanFactory1.setAllowBeanDefinitionOverriding(true);
        beanFactory1.setAllowCircularReferences(true);
    }
}
