package com.dhcc.shanjupay.transaction.api;

import com.dhcc.shanjupay.common.util.StringUtil;
import com.dhcc.shanjupay.transaction.api.dto.PayChannelDTO;
import com.dhcc.shanjupay.transaction.api.dto.PayChannelParamDTO;
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
     * 根据服务类型查询支付渠道
     *
     * @param platformChannelCode 服务类型
     * @return 支付渠道列表
     * @throws BusinessException
     */
    List<PayChannelDTO> queryPayChannelByPlatformChannel(String platformChannelCode) throws BusinessException;

    /**
     * 支付渠道参数配置
     *
     * @param payChannelParamDTO 商户id 应用id 服务类型code 支付渠道code 配置名称 配置参数（json）
     * @throws BusinessException
     */
    void savePayChannelParam(PayChannelParamDTO payChannelParamDTO) throws BusinessException;

    /**
     * 根据应用和服务类型查询支付渠道参数列表
     * @param appId
     * @param platformChannel
     * @return
     */
    List<PayChannelParamDTO> queryPayChannelParamByAppAndPlatform(String appId, String platformChannel) throws BusinessException;

    /**
     * 根据应用 服务类型 和支付渠道代码查询该支付渠道参数
     * @param appId
     * @param platfromChannel
     * @param payChannel
     * @return
     * @throws BusinessException
     */
    PayChannelParamDTO queryParamByAppPlatformAndPayChannel(String appId, String platfromChannel, String payChannel) throws BusinessException;
}
