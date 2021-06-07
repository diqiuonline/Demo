package com.enjoy.jack.test;

import com.enjoy.jack.bean.scanBean.ScanBean;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class AnnoTest {

    @Test
    public void test1() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ScanBean.class);
    }
}
