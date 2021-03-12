package com.enjoy.jack.bean;


public class FactoryMethodBean {

    public Object factoryMethod() {
        return new Jack();
    }

    public Object factoryMethod(String param) {
        return new Jack();
    }
}
