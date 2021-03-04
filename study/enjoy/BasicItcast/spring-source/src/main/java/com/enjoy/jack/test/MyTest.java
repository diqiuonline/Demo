package com.enjoy.jack.test;

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
}
