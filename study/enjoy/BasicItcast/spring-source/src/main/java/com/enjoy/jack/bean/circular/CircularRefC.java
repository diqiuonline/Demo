package com.enjoy.jack.bean.circular;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
public class CircularRefC {

    public CircularRefC() {
        System.out.println("============CircularRefA()===========");
    }

    //这里会触发CircularRefB类型的getBean操作
    @Autowired
    private CircularRefA circularRefA;

    @Autowired
    private CircularRefB circularRefB;
}
