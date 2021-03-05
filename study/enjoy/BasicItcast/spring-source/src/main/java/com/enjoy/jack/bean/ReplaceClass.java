package com.enjoy.jack.bean;

import org.springframework.beans.factory.support.MethodReplacer;

import java.lang.reflect.Method;

/**
 * @Classname ReplaceClass
 * @Description TODO
 * @Author Jack
 * Date 2020/12/10 20:49
 * Version 1.0
 */
public class ReplaceClass implements MethodReplacer {
    @Override
    public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
        System.out.println("I am replace method-->reimplement!");
        return null;
    }
}
