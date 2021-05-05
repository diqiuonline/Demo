package com.enjoy.jack.bean;

public class Woman implements People{
    @Override
    public void showsix() {
        System.out.println("i am man");
    }

    public void init() {
        System.out.println("==========Woman.init======");
    }
}
