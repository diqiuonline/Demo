package com.dhcc.shanjupay.merchant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dhcc.shanjupay.merchant.convert.MerchantConvert;
import com.dhcc.shanjupay.merchant.entity.Merchant;
import com.dhcc.shanjupay.merchant.mapper.MerchantMapper;
import com.dhcc.shanjupay.merchat.api.MerchantService;
import com.dhcc.shanjupay.merchat.api.dto.MerchantDTO;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.PhoneUtil;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/9 20:09
 */
@Service
@Transactional
public class MerchantServiceImpl implements MerchantService  {
    @Autowired
    private MerchantMapper merchantMapper;
    @Override
    public MerchantDTO queryMerchantById(Long id) {
        Merchant merchant = merchantMapper.selectById(id);

        MerchantDTO merchantDTO = MerchantConvert.INSTANCE.entityToDto(merchant);

        return merchantDTO;
    }

    /**
     * 插入商户
     *
     * @param merchantDTO
     * @return
     */
    @Override
    //@Transactional
    public MerchantDTO createMerchant(MerchantDTO merchantDTO) throws BusinessException{
        //校验参数的合法性
        if (StringUtils.isEmpty(merchantDTO)) {
            throw new BusinessException(CommonErrorCode.E_100108);
        }
        if (StringUtils.isEmpty(merchantDTO.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100108);
        }
        if (!PhoneUtil.isMatches(merchantDTO.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100109);
        }
        //校验手机号的唯一 根据手机号查询商户表 如果存在记录则说明手机号已存在
        QueryWrapper<Merchant> queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile", merchantDTO.getMobile());
        Integer count = merchantMapper.selectCount(queryWrapper);
        /*throw new BusinessException(CommonErrorCode.E_100109);*/
        if (count > 0) {
            throw new BusinessException(CommonErrorCode.E_100113);
        }

        //调用mapper想数据库写入记录
        Merchant merchant = MerchantConvert.INSTANCE.dtoToEntity(merchantDTO);
        merchant.setAuditStatus("0");
        merchantMapper.insert(merchant);
        return MerchantConvert.INSTANCE.entityToDto(merchant);
    }

    /**
     * 资质申请
     *
     * @param merchantId  商户id
     * @param merchantDTO 资质信息
     * @throws BusinessException
     */
    @Override
    public void applyMerchant(Long merchantId, MerchantDTO merchantDTO) throws BusinessException {
        if (StringUtils.isEmpty(merchantId) || StringUtils.isEmpty(merchantDTO)) {
            throw new BusinessException(CommonErrorCode.E_300009);
        }
        //校验商户id的合法性
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (StringUtils.isEmpty(merchant)) {
            throw new BusinessException(CommonErrorCode.E_200002);
        }
        Merchant merchantEnitiy = MerchantConvert.INSTANCE.dtoToEntity(merchantDTO);
        merchantEnitiy.setId(merchantId);
        merchantEnitiy.setMobile(merchant.getMobile());  //资质申请的时候 手机号不用填
        merchantEnitiy.setAuditStatus("1");  //状态待审核
        merchantEnitiy.setTenantId(merchant.getTenantId());
        merchantMapper.updateById(merchantEnitiy);
    }


}
