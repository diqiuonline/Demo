package com.enjoy.jack.bean.DisposableBean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;


@Component
public class DestoryBean implements DisposableBean {
    @Override
    public void destroy() throws Exception {
        System.out.println("==DestoryBean.destroy");
    }
}
