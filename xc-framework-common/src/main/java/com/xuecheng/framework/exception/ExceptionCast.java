package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * User: 李锦卓
 * Time: 2019/1/8 17:15
 */
public class ExceptionCast {
    //使用此静态方法抛出自定义异常
    public static void cast(ResultCode resultCode) {
        throw new CustomException(resultCode);
    }

}