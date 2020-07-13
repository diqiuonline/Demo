package com.dhcc.shanjupay.merchant.api;

import com.dhcc.shanjupay.merchant.api.dto.AppDTO;
import com.dhcc.shanjupay.common.domain.BusinessException;

import java.util.List;

/**
 * 应用管理相关的接口
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/29 22:03
 */
public interface AppService {
    //应用的信息 ，商户的id

    /**
     * 创建应用
     * @param merchantId 商户id
     * @param appDTO 应用信息
     * @return 创建成功的应用的信息
     * @throws BusinessException
     */
    AppDTO createApp(Long merchantId,AppDTO appDTO) throws BusinessException;

    //更具商户id查询应用
    List<AppDTO> queryAppByMerchant(Long merchantId) throws BusinessException;

    //根据应用id查询应用
    AppDTO getAppById(String appId) throws BusinessException;


}
