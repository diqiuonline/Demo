package com.dhcc.test;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
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


    @AfterReturning(value = "pointCut()",returning="returnValue")
    public void advice(JoinPoint pjp,Object returnValue) {
        //pjp.getArgs()
        //System.out.println("aop --before--advice");
        //return pjp;
    }
}
