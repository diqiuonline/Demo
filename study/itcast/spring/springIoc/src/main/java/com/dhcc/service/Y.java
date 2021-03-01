package com.dhcc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/4/14 23:35
 */
@Component
public class Y {
    @Autowired
    private X x;

    public Y() {
        System.out.println("create Y");
    }
}
