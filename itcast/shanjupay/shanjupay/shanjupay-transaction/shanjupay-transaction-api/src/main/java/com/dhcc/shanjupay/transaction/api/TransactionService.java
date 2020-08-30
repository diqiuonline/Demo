package com.dhcc.shanjupay.transaction.api;

import com.dhcc.shanjupay.common.domain.BusinessException;
import com.dhcc.shanjupay.paymentagent.api.dto.PaymentResponseDTO;
import com.dhcc.shanjupay.transaction.api.dto.PayOrderDTO;
import com.dhcc.shanjupay.transaction.api.dto.QRCodeDto;

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
}
