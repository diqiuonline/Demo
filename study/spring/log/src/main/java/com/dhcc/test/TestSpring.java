package com.dhcc.test;

import com.dhcc.IndexService;
import com.dhcc.config.Appconfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/2/14 23:49
 * spring 5 默认是log4j2，还支持slf4j  不支持log4j   spring4 支持log4f。 没有引入log框架默认使用jul
 */
public class TestSpring {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(Appconfig.class);
        IndexService indexService = annotationConfigApplicationContext.getBean(IndexService.class);
        indexService.index();
    }
}
