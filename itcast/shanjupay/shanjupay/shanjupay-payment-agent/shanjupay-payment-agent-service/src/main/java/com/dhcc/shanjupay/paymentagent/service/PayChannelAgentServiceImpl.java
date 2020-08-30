package com.dhcc.shanjupay.paymentagent.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.dhcc.shanjupay.common.domain.BusinessException;
import com.dhcc.shanjupay.common.domain.CommonErrorCode;
import com.dhcc.shanjupay.paymentagent.api.PayChannelAgentService;
import com.dhcc.shanjupay.paymentagent.api.conf.AliConfigParam;
import com.dhcc.shanjupay.paymentagent.api.dto.AlipayBean;
import com.dhcc.shanjupay.paymentagent.api.dto.PaymentResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/8/23 12:14
 */
@Service
@Slf4j
public class PayChannelAgentServiceImpl implements PayChannelAgentService {
    //应用id

    @Override
    public PaymentResponseDTO createPayOrderByAliWAP(AliConfigParam aliConfigParam, AlipayBean alipayBean) throws BusinessException {
        String APP_ID = aliConfigParam.getAppId();
        //应用私钥
        String APP_PRIVATE_KEY = aliConfigParam.getRsaPrivateKey();
        //支付宝公钥
        String ALIPAY_PUBLIC_KEY = aliConfigParam.getAlipayPublicKey();
        String CHARSET = aliConfigParam.getCharest();
        //支付宝接口的网关地址，正式"https://openapi.alipay.com/gateway.do"
        String serverUrl = aliConfigParam.getUrl();
        String format = aliConfigParam.getFormat();
        String returnUrl = aliConfigParam.getReturnUrl();//支付成功跳转url
        String notifyUrl = aliConfigParam.getNotifyUrl();//支付结果异步通之url
        //签名算法类型
        String signtype = aliConfigParam.getSigntype();


        AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, APP_ID, APP_PRIVATE_KEY, format, CHARSET, ALIPAY_PUBLIC_KEY, signtype);
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        AlipayTradeWapPayModel alipayTradeWapPayModel = new AlipayTradeWapPayModel();
        alipayTradeWapPayModel.setOutTradeNo(alipayBean.getOutTradeNo()); // 商户的订单，就是闪聚平台的订单
        alipayTradeWapPayModel.setTotalAmount(alipayBean.getTotalAmount()); //订单金额 元
        alipayTradeWapPayModel.setSubject(alipayBean.getSubject()); //订单标题
        alipayTradeWapPayModel.setBody(alipayBean.getBody()); //订单描述
        alipayTradeWapPayModel.setTimeoutExpress(alipayBean.getExpireTime()); //订单过期时间
        alipayTradeWapPayModel.setProductCode("QUICK_WAP_WAY");

        alipayRequest.setBizModel(alipayTradeWapPayModel);
        alipayRequest.setReturnUrl(returnUrl);
        alipayRequest.setNotifyUrl(notifyUrl);
        String form = "";
        try {
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(alipayRequest);
            PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
            paymentResponseDTO.setContent(response.getBody());  //支付宝的响应结果
            return paymentResponseDTO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorCode.E_400002);
        }
    }
}
