package com.enjoy.jack.bean;

import lombok.Data;



@Data
public class Jack {
    public String name = "jack";

    public static Jack factoryMethod() {
        return new Jack();
    }
}
