package com.dhcc.service;

import org.springframework.stereotype.Component;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/2/18 20:28
 */
@Component("c")
public class CityService implements L {

    @Override
    public void query() {
        System.out.println("query db ----");
    }
}
