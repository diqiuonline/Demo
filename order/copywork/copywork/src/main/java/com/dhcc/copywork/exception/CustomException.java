package com.dhcc.copywork.exception;

import com.dhcc.copywork.entity.response.ResultCode;

/**
 * User: 李锦卓
 * Time: 2019/1/8 17:00
 */
public class CustomException extends RuntimeException {
    private ResultCode resultCode;

    public CustomException(ResultCode resultCode,String msg) {
        //异常信息为错误代码+异常信息
        super("错误代码:"+resultCode.code()+"错误信息:"+msg);
      this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return this.resultCode;
    }
}