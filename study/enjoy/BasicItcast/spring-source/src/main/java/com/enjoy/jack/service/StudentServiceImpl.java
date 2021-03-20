package com.enjoy.jack.service;

import com.enjoy.jack.annotation.EasyCache;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StudentServiceImpl implements StudentService {



    @Override
    public void eat(String a) {
        System.out.println("=====StudentServiceImpl.eat");
    }

}
