package com.dhcc.service;

import com.dhcc.test.dhccAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/2/18 20:28
 */
@Component("c")
//@EnableAspectJAutoProxy(proxyTargetClass= true)
public class CityService implements L {

    @Autowired
    private dhccAspect dhccAspect;

    public CityService() {
        System.out.println("原生电影被拍摄");
    }

    @Override

    public void query() {
        System.out.println("query db ----");
    }
}
