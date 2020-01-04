package com.dhcc.web.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * User: 李锦卓
 * Time: 2019/2/19 22:03
 */
//@ControllerAdvice
/*
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Map<String,Object> getException(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("errorCode", "103");
        map.put("errorMsg", "系统错误");
        return map;
    }
}*/
