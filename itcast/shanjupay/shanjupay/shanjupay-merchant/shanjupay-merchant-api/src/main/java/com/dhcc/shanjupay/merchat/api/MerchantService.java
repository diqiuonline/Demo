package com.dhcc.shanjupay.merchat.api;

import com.dhcc.shanjupay.merchat.api.dto.MerchantDTO;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/9 20:01
 */
public interface MerchantService {
    public MerchantDTO queryMerchantById(Long id);
}
