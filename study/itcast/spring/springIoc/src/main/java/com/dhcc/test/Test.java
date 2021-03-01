package com.dhcc.test;

import com.dhcc.config.Appconfig;
import com.dhcc.service.X;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/4/14 23:36
 */
public class Test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(Appconfig.class);
        X bean = (X) ac.getBean("x");
        System.out.println(bean);
    }
}
