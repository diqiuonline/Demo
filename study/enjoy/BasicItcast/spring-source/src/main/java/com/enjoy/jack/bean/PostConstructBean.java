package com.enjoy.jack.bean;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@Component
public class PostConstructBean {

    @PostConstruct
    public void init() {
        System.out.println("----init");

    }

    @PreDestroy
    public void desy() {
        System.out.println("----desy");
    }
}
