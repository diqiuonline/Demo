package com.dhcc.shanjupay.transaction.api;

import com.dhcc.shanjupay.transaction.api.dto.PayChannelDTO;
import com.dhcc.shanjupay.transaction.api.dto.PlatformChannelDTO;

import java.util.List;
import com.dhcc.shanjupay.common.domain.BusinessException;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/7/8 19:27
 */
public interface PayChannelService {
    //查询平台的服务类型
    List<PlatformChannelDTO> queryPlatformChannerl() throws BusinessException;

    /**
     * @param appId                应用id
     * @param platformChannelCodes 服务类型的code
     * @throws BusinessException
     */
    void bindPlatformChannelForApp(String appId, String platformChannelCodes) throws BusinessException;

    /**
     * @param appId                应哟id
     * @param platformChannelCodes 服务类型code
     * @return 已绑定1 否则0
     * @throws BusinessException
     */
    int queryAppBindPlatformChannel(String appId, String platformChannelCodes) throws BusinessException;

    /**
     *根据服务类型查询支付渠道
     * @param platformChannelCode 服务类型
     * @return 支付渠道列表
     * @throws BusinessException
     */
    List<PayChannelDTO> queryPayChannelByPlatformChannel(String platformChannelCode) throws BusinessException;
}
