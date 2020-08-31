package com.dhcc.copywork.exception;

import com.dhcc.copywork.entity.response.ResponseResult;
import com.dhcc.copywork.entity.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * User: 李锦卓
 * Time: 2019/1/8 17:17
 */
@RestControllerAdvice
public class ExceptionCatch {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionCatch.class);

    //捕获CustomException异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult customException(CustomException e) {
        ResultCode resultCode = e.getResultCode();
        ResponseResult responseResult = new ResponseResult(resultCode,e.getMessage());
        return responseResult;
    }


}