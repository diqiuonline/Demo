package com.enjoy.jack.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Classname EnjoyApplicationListener
 * @Description TODO
 * @Author Jack
 * Date 2020/12/17 14:03
 * Version 1.0
 */
//@Component
public class EnjoyApplicationListener implements ApplicationListener {




    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof EnjoyEvent) {
            System.out.println("==EnjoyApplicationListener");
        }
    }
}
