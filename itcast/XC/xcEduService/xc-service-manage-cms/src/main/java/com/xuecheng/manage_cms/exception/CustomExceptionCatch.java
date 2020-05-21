package com.xuecheng.manage_cms.exception;

import com.xuecheng.framework.exception.ExceptionCatch;
import com.xuecheng.framework.model.response.CommonCode;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.lang.reflect.InvocationTargetException;

/**
 * User: 李锦卓
 * Time: 2019/5/26 20:45
 * 课程管理自定义的异常类，其中定义异常类型所对应的错误代码
 */
@ControllerAdvice
public class CustomExceptionCatch extends ExceptionCatch {
    static {
        builder.put(InvocationTargetException.class, CommonCode.UNAUTHORISE);
    }
}