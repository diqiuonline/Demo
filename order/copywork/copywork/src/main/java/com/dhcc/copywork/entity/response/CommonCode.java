package com.dhcc.copywork.entity.response;

import lombok.ToString;

/**
 * @Author: mrt.
 * @Description:
 * @Date:Created in 2018/1/24 18:33.
 * @Modified By:
 */

@ToString
public enum CommonCode implements ResultCode {
    SUCCESS(true,10000),
    FAIL(false,0);
    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    CommonCode(boolean success, int code){
        this.success = success;
        this.code = code;
    }

    @Override
    public boolean success() {
        return success;
    }
    @Override
    public int code() {
        return code;
    }



}
