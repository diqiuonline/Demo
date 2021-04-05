package com.enjoy.jack.bean;

import com.enjoy.jack.designPattern.strategy.CQ;
import com.enjoy.jack.designPattern.strategy.SC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AutowiredConstructorBean {
   /* @Autowired(required = false)
    public AutowiredConstructorBean(SC sc, CQ cq) {
        System.out.println(sc);
        System.out.println(cq);
    }



    @Autowired(required = false)
    public AutowiredConstructorBean(SC sc) {
        System.out.println(sc);
    }*/

    public AutowiredConstructorBean() {
    }

    public AutowiredConstructorBean(SC sc, CQ cq) {
        System.out.println(sc);
        System.out.println(cq);
    }
    public AutowiredConstructorBean(SC sc) {
        System.out.println(sc);
    }
}
