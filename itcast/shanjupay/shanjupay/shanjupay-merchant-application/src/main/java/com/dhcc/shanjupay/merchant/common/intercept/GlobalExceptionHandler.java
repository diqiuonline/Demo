package com.dhcc.shanjupay.merchant.common.intercept;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.domain.ErrorCode;
import com.shanjupay.common.domain.RestErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理器
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/15 19:53
 */
@ControllerAdvice//与@Exceptionhandler配合使用实现全局异常处理
@Slf4j

public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //捕获Exception异常
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse processExcetion(HttpServletRequest request,
                                             HttpServletResponse response,
                                             Exception e){
        //解析异常信息
        //如果是系统自定义异常，直接取出errCode和errMessage
        if(e instanceof BusinessException){

            //LOGGER.info("错误代码：" +  + "错误内容" + ((BusinessException) e).getMessage();
            //解析系统自定义异常信息
            BusinessException businessException= (BusinessException) e;
            ErrorCode errorCode = businessException.getErrorCode();
            //错误代码
            int code = errorCode.getCode();
            //错误信息
            String desc = errorCode.getDesc();
            log.error("错误代码："+code+"错误内容："+desc);
            return new RestErrorResponse(String.valueOf(code),desc);
        }

        log.error("系统异常："+e.getMessage());
        //统一定义为99999系统未知错误
        return new RestErrorResponse(String.valueOf(CommonErrorCode.UNKNOWN.getCode()),CommonErrorCode.UNKNOWN.getDesc());

    }
}
