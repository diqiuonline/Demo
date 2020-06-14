package com.dhcc.shanjupay.merchant.service;

import com.dhcc.shanjupay.merchant.convert.MerchantConvert;
import com.dhcc.shanjupay.merchant.entity.Merchant;
import com.dhcc.shanjupay.merchant.mapper.MerchantMapper;
import com.dhcc.shanjupay.merchat.api.MerchantService;
import com.dhcc.shanjupay.merchat.api.dto.MerchantDTO;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/9 20:09
 */
@Service
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    private MerchantMapper merchantMapper;
    @Override
    public MerchantDTO queryMerchantById(Long id) {
        Merchant merchant = merchantMapper.selectById(id);

        MerchantDTO merchantDTO = MerchantConvert.INSTANCE.entityToDto(merchant);

        return merchantDTO;
    }

    @Override
    public MerchantDTO createMerchant(MerchantDTO merchantDTO) {
        //调用mapper想数据库写入记录

        Merchant merchant = MerchantConvert.INSTANCE.dtoToEntity(merchantDTO);
        merchant.setAuditStatus("0");
        merchantMapper.insert(merchant);
        return MerchantConvert.INSTANCE.entityToDto(merchant);
    }
}
