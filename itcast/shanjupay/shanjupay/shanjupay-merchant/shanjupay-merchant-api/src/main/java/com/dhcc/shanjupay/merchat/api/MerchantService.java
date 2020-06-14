package com.dhcc.shanjupay.merchat.api;

import com.dhcc.shanjupay.merchat.api.dto.MerchantDTO;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/9 20:01
 */
public interface MerchantService {
    //根据id查询商户
    public MerchantDTO queryMerchantById(Long id);
    //注册商户 接受账号密码  使用MerchantDTO接受数据
    public MerchantDTO createMerchant(MerchantDTO merchantDTO);

}
