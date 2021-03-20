package com.enjoy.jack.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Classname EnjoyApplicationListener
 * @Description TODO
 * @Author Jack
 * Date 2020/12/17 14:03
 * Version 1.0
 */
@Component
public class EnjoyApplicationListener1 implements ApplicationListener {




    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof EnjoyEvent1) {
            System.out.println("==EnjoyApplicationListener1");
        }
    }
}
