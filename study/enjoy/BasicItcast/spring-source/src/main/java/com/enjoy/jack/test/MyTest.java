package com.enjoy.jack.test;

import com.enjoy.jack.bean.*;
import com.enjoy.jack.bean.circular.CircularRefConA;
import com.enjoy.jack.bean.circular.CircularRefConB;
import com.enjoy.jack.bean.circular.CircularRefPrototypeA;
import com.enjoy.jack.bean.circular.CircularRefPrototypeB;
import com.enjoy.jack.bean.propertiesbean.PropertiesBean;
import com.enjoy.jack.beanDefinitionPostProcessor.PlaceHolderBean1;
import com.enjoy.jack.beans.Teacher;
import com.enjoy.jack.customBean.James13;
import com.enjoy.jack.designPattern.strategy.CQ;
import com.enjoy.jack.event.EnjoyApplicationListener;
import com.enjoy.jack.event.EnjoyEvent;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @Classname MyTest
 * @Description TODO
 * @Author Jack
 * Date 2020/12/7 15:42
 * Version 1.0
 */
public class MyTest {

    @Test
    public void test1() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        Student bean = applicationContext.getBean(Student.class);
        System.out.println(bean.getUsername());
    }

    @Test
    public void test2() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        ShowSixClass bean = (ShowSixClass)applicationContext.getBean("people");
        bean.showsex();
    }

    @Test
    public void test3() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        OriginClass bean = (OriginClass)applicationContext.getBean("originClass");
        bean.method("2345");
    }

    @Test
    public void test4() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        Jack bean = applicationContext.getBean(Jack.class);
        System.out.println(bean);
    }

    @Test
    public void test5() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        CQ bean = applicationContext.getBean(CQ.class);
        System.out.println(bean);
    }




    @Test
    public void test6() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        BeanDefinitionBean bean = applicationContext.getBean(BeanDefinitionBean.class);
        System.out.println(bean.getName());
    }


    @Test
    public void test7() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        James13 bean = applicationContext.getBean(James13.class);
        System.out.println(bean);
    }


    @Test
    public void test8() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestBean.class);
        Teacher teacher = applicationContext.getBean(Teacher.class);
        System.out.println(teacher.getUsername());
    }

    @Test
    public void test9() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        applicationContext.addApplicationListener(new EnjoyApplicationListener());
        applicationContext.publishEvent(new EnjoyEvent("Jack", "enjoyEvent"));

        applicationContext.start();
        applicationContext.stop();

       /* SimpleApplicationEventMulticaster bean = applicationContext.getBean(SimpleApplicationEventMulticaster.class);
        bean.setTaskExecutor(null);*/

    }

    @Test
    public void test10() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        //手动创建了实例并交给spring管理
        applicationContext.getBeanFactory().registerSingleton("jack", new Jack());
        //((DefaultListableBeanFactory)applicationContext.getBeanFactory()).destroySingleton("jack");
        Jack bean = applicationContext.getBean(Jack.class);
        System.out.println(bean.getName());

    }

    @Test
    public void test11() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        Jack bean = applicationContext.getBean(Jack.class);

        System.out.println(bean.getSc());

    }

    @Test
    public void test12() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        CircularRefConA bean = applicationContext.getBean(CircularRefConA.class);
        CircularRefConB bean3 = applicationContext.getBean(CircularRefConB.class);
        System.out.println(bean.toString());

    }

    @Test
    public void test13() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        CircularRefPrototypeA bean = applicationContext.getBean(CircularRefPrototypeA.class);
        CircularRefPrototypeB bean3 = applicationContext.getBean(CircularRefPrototypeB.class);
        System.out.println(bean.toString());

    }

    @Test
    public void test14() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        applicationContext.getBeanFactory().destroyBean("jack");
        applicationContext.getBeanFactory().destroySingletons();
        //System.out.println(bean.toString());

    }
    @Test
    public void test15() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        PropertiesBean bean = applicationContext.getBean(PropertiesBean.class);
        System.out.println(bean.getName());
        System.out.println(bean.getPassword());

    }

    @Test
    public void test16() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        Environment bean = applicationContext.getBean(Environment.class);
        System.out.println(bean);

    }

    @Test
    public void test17() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        System.out.println(applicationContext.getBean(PlaceHolderBean1.class));

    }
}
