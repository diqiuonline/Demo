package com.dhcc.shanjupay.merchant.service;

import com.dhcc.shanjupay.common.domain.BusinessException;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/14 22:23
 */
public interface SmsService {
    /**
     * 发送手机验证码
     * @param phone
     * @return
     */
    String SendMsg(String phone);

    void checkVerifiyCode(String verifiyKey, String verifiyCode) ;

}
