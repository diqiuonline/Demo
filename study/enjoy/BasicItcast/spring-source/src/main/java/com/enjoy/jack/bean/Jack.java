package com.enjoy.jack.bean;

import com.enjoy.jack.designPattern.strategy.CQ;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;



@Data
public class Jack {

    @Autowired
    private CQ cq;

    public String name = "jack";

    public static Jack factoryMethod() {
        return new Jack();
    }

    public static Jack factoryMethod(String param) {
        return new Jack();
    }
}
