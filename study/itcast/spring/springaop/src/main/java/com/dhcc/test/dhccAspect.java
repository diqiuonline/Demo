package com.dhcc.test;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/2/18 21:22
 */
@Component
@Aspect
public class dhccAspect {
    @Pointcut("execution(* com.dhcc.service..*.*(..))")
    public void pointCut() {
    }


    @Before("pointCut()")
    public void advice(JoinPoint pjp) {
        System.out.println("aop --before--advice");
    }
}
