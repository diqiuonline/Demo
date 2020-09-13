package com.dhcc.shanjupay.transaction.api;

import com.dhcc.shanjupay.common.domain.BusinessException;
import com.dhcc.shanjupay.paymentagent.api.dto.PaymentResponseDTO;
import com.dhcc.shanjupay.transaction.api.dto.PayOrderDTO;
import com.dhcc.shanjupay.transaction.api.dto.QRCodeDto;

import java.util.Map;

/**
 * 交易服务相关的接口
 * @author 李锦卓
 * @version 1.0
 * @date 2020/8/18 15:20
 */
public interface TransactionService {
    /**
     * 生成门店二维码的url
     *
     * @param qrCodeDto merchantId appId storeId channel subject body
     * @return 支付入口(url) 要携带参数 将传入的参数转成json 用base64编码
     * @throws BusinessException
     */
    String createStoreQRCode(QRCodeDto qrCodeDto) throws BusinessException;
    /**
     * 保存支付宝订单，1、保存订单到闪聚平台，2、调用支付渠道代理服务调用支付宝的接口
     *
     * @param payOrderDTO
     * @return
     * @throws BusinessException
     */
    PaymentResponseDTO submitOrderByAli(PayOrderDTO payOrderDTO) throws BusinessException;

    /**
     * 更具订单号查询订单信息
     *
     * @param tradeNo
     * @return
     */
    PayOrderDTO queryPayOrder(String tradeNo);
    /**
     * 更新订单支付状态
     *
     * @param tradeNo           闪聚平台订单号
     * @param payChannelTradeNo 支付宝或微信的交易流水号(第三方支付系统的订单)
     * @param state             订单状态  交易状态支付状态,0-订单生成,1-支付中(目前未使用),2-支付成功,4-关闭 5--失败
     */
    public void updateOrderTradeNoAndTradeState(String tradeNo, String payChannelTradeNo, String state) throws BusinessException;

    /**
     * 申请微信授权码
     * @param payOrderDTO
     * @return 申请授权码的地址
     */
    public String getWXOAuth2Code(PayOrderDTO payOrderDTO);

    /**
     * 申请openid
     *
     * @param code  授权码
     * @param appId 闪聚平台的应用id，为了获取该应用的微信支付渠道参数
     * @return
     */
    public String getWXOAuthOpenId(String code, String appId);
    /**
     * 1、保存订单到闪聚平台，2、调用支付渠道代理服务调用微信的接口
     * @param payOrderDTO
     * @return h5页面所需要的数据
     */
    Map<String, String> submitOrderByWechat(PayOrderDTO payOrderDTO) throws BusinessException;
}
