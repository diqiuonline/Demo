package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * User: 李锦卓
 * Time: 2019/1/8 17:17
 */
@ControllerAdvice
public class ExceptionCatch {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionCatch.class);

    //捕获CustomException异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException e) {
        ResultCode resultCode = e.getResultCode();
        ResponseResult responseResult = new ResponseResult(resultCode);
        return responseResult;
    }

    //使用EXCEPTIONS存放异常类型和错误代码映射 ImmutableMap的特点是一旦创建不可改变 并且线程安全
    // 定义map 配置异常类型所对应的错误代码
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;
    //定义map的builder对象 去构建ImmutableMap对象
    protected static ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> builder = ImmutableMap.builder();

    //使用build来构建一个y异常类型和错误代码异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e) {
        if (EXCEPTIONS == null) {
            EXCEPTIONS = builder.build(); //EXCEPTIONS构建成功
        }
        //从EXCEPTIONS中找异常类型所对因的错误代码 如果找到了将错误代码相应给用户 如果找不到响应9999异常
        ResultCode resultCode = EXCEPTIONS.get(e.getClass());
        if (resultCode != null) {
            return new ResponseResult(resultCode);
        } else {
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }
    }

    static {
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM);
    }
}