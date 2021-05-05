package com.enjoy.jack.bean.circular;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
public class CircularRefB {

    public CircularRefB() {
        System.out.println("============CircularRefB()===========");
    }

    //又会触发A的getBean操作
    @Autowired
    private CircularRefA circularRefA;

    @Autowired
    private CircularRefC circularRefC;

}
