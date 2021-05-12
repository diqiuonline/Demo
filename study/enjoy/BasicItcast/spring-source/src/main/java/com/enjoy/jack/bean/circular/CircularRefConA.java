package com.enjoy.jack.bean.circular;

import lombok.Data;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Data
@Component
public class CircularRefConA {
    private CircularRefConB circularRefConB;

    @Lazy
    //会触发入参对象的getBean
    public CircularRefConA(CircularRefConB circularRefConB) {

        this.circularRefConB = circularRefConB;
        //circularRefConB.getB();
        System.out.println("============CircularRefConA()===========");
    }
    public void getA() {

    }
}
