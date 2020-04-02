package com.dhcc.test;

import com.dhcc.config.Appconfig;
import com.dhcc.service.L;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/2/18 20:46
 */
public class test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(Appconfig.class);
        L l = (L) ac.getBean("c");
        l.query();
    }
}
