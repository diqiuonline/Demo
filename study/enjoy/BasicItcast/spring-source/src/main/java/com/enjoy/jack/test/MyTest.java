package com.enjoy.jack.test;

import com.enjoy.jack.bean.OriginClass;
import com.enjoy.jack.bean.ShowSixClass;
import com.enjoy.jack.bean.Student;
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
}
