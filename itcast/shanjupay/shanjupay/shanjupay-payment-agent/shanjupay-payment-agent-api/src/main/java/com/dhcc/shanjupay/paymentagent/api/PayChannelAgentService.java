package com.dhcc.shanjupay.paymentagent.api;

import com.dhcc.shanjupay.common.domain.BusinessException;
import com.dhcc.shanjupay.paymentagent.api.conf.AliConfigParam;
import com.dhcc.shanjupay.paymentagent.api.conf.WXConfigParam;
import com.dhcc.shanjupay.paymentagent.api.dto.AlipayBean;
import com.dhcc.shanjupay.paymentagent.api.dto.PaymentResponseDTO;
import com.dhcc.shanjupay.paymentagent.api.dto.WeChatBean;

import java.util.Map;

/**
 * 与第三方支付渠道进行交互
 * @author 李锦卓
 * @version 1.0
 * @date 2020/8/23 12:12
 */
public interface PayChannelAgentService {
    /**
     * 调用支付宝的下单接口
     * @param aliConfigParam 支付渠道配置的参数（配置的支付宝的必要参数）
     * @param alipayBean 业务参数（商户订单号，订单标题，订单描述,,）
     * @return 统一返回PaymentResponseDTO
     */
    public PaymentResponseDTO createPayOrderByAliWAP(AliConfigParam aliConfigParam, AlipayBean alipayBean) throws BusinessException;

    /**
     * 查询支付宝订单状态
     * @param aliConfigParam 支付渠道参数
     * @param outTradeNo 闪聚平台的订单号
     * @return
     */
    public PaymentResponseDTO queryPayOrderByAli(AliConfigParam aliConfigParam,String outTradeNo) throws BusinessException;



    /**
     * 查询微信订单状态
     * @param wxConfigParam 支付渠道参数
     * @param outTradeNo 闪聚平台的订单号
     * @return
     * @throws BusinessException
     */
    public PaymentResponseDTO queryPayOrderByWeChat(WXConfigParam wxConfigParam, String outTradeNo)throws BusinessException;
    /**
     * 微信下单接口
     * @param wxConfigParam 微信支付渠道参数
     * @param weChatBean 订单业务数据
     * @return h5网页的数据
     */
    public Map<String, String> createPayOrderByWeChatJSAPI(WXConfigParam wxConfigParam, WeChatBean weChatBean);
}
