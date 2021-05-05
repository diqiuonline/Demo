package com.enjoy.jack.aware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;


@Component
public class AwareBean implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, EnvironmentAware, ImportAware, InitializingBean {
    @Override
    public void setBeanName(String name) {
        System.out.println(name);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory beanFactory1 = (DefaultListableBeanFactory)beanFactory;
        beanFactory1.setAllowCircularReferences(true);
        System.out.println(beanFactory);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println(applicationContext);
    }

    @Override
    public void setEnvironment(Environment environment) {
        System.out.println(environment);
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        //这个方法就是要拿到注解的值
        MergedAnnotations annotations = importMetadata.getAnnotations();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("========afterPropertiesSet");
    }
}