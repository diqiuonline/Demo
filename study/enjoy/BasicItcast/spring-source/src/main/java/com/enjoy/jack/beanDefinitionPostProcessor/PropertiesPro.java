package com.enjoy.jack.beanDefinitionPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.stereotype.Component;

import java.util.Properties;


//@Component
public class PropertiesPro implements BeanDefinitionRegistryPostProcessor,EnvironmentAware {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
       /* StandardEnvironment bean = (StandardEnvironment)beanFactory.getBean(Environment.class);
        Properties properties = new Properties();
        properties.put("enjoy.name","James");
        PropertiesPropertySource propertiesCustom = new PropertiesPropertySource("propertiesCustom", properties);
        MutablePropertySources propertySources = bean.getPropertySources();
        propertySources.addLast(propertiesCustom);*/
    }


    @Override
    public void setEnvironment(Environment environment) {
        StandardEnvironment bean = (StandardEnvironment)environment;
        Properties properties = new Properties();
        properties.put("enjoy.name","James");
        PropertiesPropertySource propertiesCustom = new PropertiesPropertySource("propertiesCustom", properties);
        MutablePropertySources propertySources = bean.getPropertySources();
        propertySources.addLast(propertiesCustom);
    }
}
