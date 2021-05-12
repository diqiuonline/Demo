package com.enjoy.jack.bean.circular;

import lombok.Data;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Data
@Component
public class CircularRefConB {
    private CircularRefConA circularRefConA;
    @Lazy
    //会触发入参对象的getBean
    public CircularRefConB(CircularRefConA circularRefConA) {
        this.circularRefConA = circularRefConA;
        circularRefConA.getA();
        System.out.println("============CircularRefConB()===========");
    }
    public void getB() {

    }
}
