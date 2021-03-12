package com.enjoy.jack.test;

import com.enjoy.jack.bean.*;
import com.enjoy.jack.customBean.James13;
import com.enjoy.jack.designPattern.strategy.CQ;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
}
