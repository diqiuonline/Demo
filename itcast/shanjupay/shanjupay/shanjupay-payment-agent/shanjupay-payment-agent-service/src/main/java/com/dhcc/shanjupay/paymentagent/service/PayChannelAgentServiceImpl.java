package com.dhcc.shanjupay.paymentagent.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.dhcc.shanjupay.common.domain.BusinessException;
import com.dhcc.shanjupay.common.domain.CommonErrorCode;
import com.dhcc.shanjupay.paymentagent.api.PayChannelAgentService;
import com.dhcc.shanjupay.paymentagent.api.conf.AliConfigParam;
import com.dhcc.shanjupay.paymentagent.api.conf.WXConfigParam;
import com.dhcc.shanjupay.paymentagent.api.dto.AlipayBean;
import com.dhcc.shanjupay.paymentagent.api.dto.PaymentResponseDTO;
import com.dhcc.shanjupay.paymentagent.api.dto.TradeStatus;
import com.dhcc.shanjupay.paymentagent.api.dto.WeChatBean;
import com.dhcc.shanjupay.paymentagent.common.constant.AliCodeConstants;
import com.dhcc.shanjupay.paymentagent.config.WXSDKConfig;
import com.dhcc.shanjupay.paymentagent.message.PayProducer;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/8/23 12:14
 */
@Service
@Slf4j
public class PayChannelAgentServiceImpl implements PayChannelAgentService {
    //应用id
    @Autowired
    private PayProducer payProducer;

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
        try {
            //调用支付宝下单接口，发起http请求
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(alipayRequest);
            PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
            paymentResponseDTO.setContent(response.getBody());  //支付宝的响应结果

            //向MQ发一条延迟消息 支付结果查询
            PaymentResponseDTO<AliConfigParam> notic = new PaymentResponseDTO<AliConfigParam>();
            notic.setOutTradeNo(alipayBean.getOutTradeNo());//闪聚平台的订单号
            notic.setContent(aliConfigParam);
            notic.setMsg("ALIPAY_WAP");//标识是查询支付宝的接口
            //发送消息
            payProducer.payOrderNotice(notic);
            return paymentResponseDTO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorCode.E_400002);
        }
    }
    /**
     * 查询支付宝订单状态
     * @param aliConfigParam 支付渠道参数
     * @param outTradeNo 闪聚平台的订单号
     * @return
     */
    @Override
    public PaymentResponseDTO queryPayOrderByAli(AliConfigParam aliConfigParam, String outTradeNo) throws BusinessException {
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
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeWapPayModel alipayTradeWapPayModel = new AlipayTradeWapPayModel();
        alipayTradeWapPayModel.setOutTradeNo(outTradeNo);
        request.setBizModel(alipayTradeWapPayModel);
        //请求支付宝的订单状态查询接口
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
            String code = response.getCode(); //支付宝相应的code 10000表试调用成功
            if (AliCodeConstants.SUCCESSCODE.equals(code)) {
                //解析支付宝返回的状态，解析成闪聚平台的taradstatus
                String tradeStatus = response.getTradeStatus();
                TradeStatus tradeStatus1 = converAliTradeStatusToShanjuCode(tradeStatus);
                //String tradeNo(支付宝的订单号), String outTradeNo（闪聚平台的订单号）, TradeStatus tradeState 订单状态, String msg 返回信息
                return PaymentResponseDTO.success(response.getTradeNo(), response.getOutTradeNo(), tradeStatus1, response.getMsg());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        //String msg, String outTradeNo, TradeStatus tradeState
        return PaymentResponseDTO.fail("支付宝订单西状态信息查询失败",outTradeNo,TradeStatus.UNKNOWN);
    }
    /**
     * 查询微信订单状态
     * @param wxConfigParam 支付渠道参数
     * @param outTradeNo 闪聚平台的订单号
     * @return
     * @throws BusinessException
     */
    @Override
    public PaymentResponseDTO queryPayOrderByWeChat(WXConfigParam wxConfigParam, String outTradeNo) throws BusinessException {
        WXSDKConfig config = new WXSDKConfig(wxConfigParam);

        Map<String, String> result = null;

        try {
            //创建sdk客户端
            WXPay wxPay = new WXPay(config);
            Map<String,String> map = new HashMap<>();
            map.put("out_trade_no",outTradeNo);//闪聚平台的订单号
            //调用微信的订单查询接口
            result = wxPay.orderQuery(map);
        } catch (Exception e) {
            e.printStackTrace();
            return PaymentResponseDTO.fail("调用微信订单查询接口失败",outTradeNo,TradeStatus.UNKNOWN);
        }

        String return_code = result.get("return_code");
        String return_msg = result.get("return_msg");
        String result_code = result.get("result_code");
        String trade_state = result.get("trade_state");//订单状态
        String transaction_id = result.get("transaction_id");//微信订单号

        if("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)){

            if("SUCCESS".equals(trade_state)){  //支付成功
                return PaymentResponseDTO.success(transaction_id,outTradeNo,TradeStatus.SUCCESS,return_msg);
            }else if("CLOSED".equals(trade_state)){//交易关闭
                return PaymentResponseDTO.success(transaction_id,outTradeNo,TradeStatus.REVOKED,return_msg);
            }else if("USERPAYING".equals(trade_state)){//支付中
                return PaymentResponseDTO.success(transaction_id,outTradeNo,TradeStatus.USERPAYING,return_msg);
            }else if("PAYERROR".equals(trade_state)){//支付失败
                return PaymentResponseDTO.success(transaction_id,outTradeNo,TradeStatus.FAILED,return_msg);
            }
        }
        return PaymentResponseDTO.success("不可识别的微信订单状态",transaction_id,outTradeNo,TradeStatus.UNKNOWN);
    }
    /**
     * 微信下单接口
     * @param wxConfigParam 微信支付渠道参数
     * @param weChatBean 订单业务数据
     * @return h5网页的数据
     */
    @Override
    public Map<String, String> createPayOrderByWeChatJSAPI(WXConfigParam wxConfigParam, WeChatBean weChatBean) {
        WXSDKConfig config = new WXSDKConfig(wxConfigParam);
        Map<String,String> jsapiPayParam = null;
        try {
            //创建sdk客户端
            WXPay wxPay = new WXPay(config);
            //构造请求的参数
            Map<String,String> requestParam = new HashMap<>();
            requestParam.put("out_trade_no",weChatBean.getOutTradeNo());//订单号
            requestParam.put("body", weChatBean.getBody());//订单描述
            requestParam.put("fee_type", "CNY");//人民币
            requestParam.put("total_fee", String.valueOf(weChatBean.getTotalFee())); //金额
            requestParam.put("spbill_create_ip", weChatBean.getSpbillCreateIp());//客户端ip
            requestParam.put("notify_url", weChatBean.getNotifyUrl());//微信异步通知支付结果接口，暂时不用
            requestParam.put("trade_type", "JSAPI");
            //从请求中获取openid
            String openid = weChatBean.getOpenId();
            requestParam.put("openid",openid);
            //调用统一下单接口
            Map<String, String> resp = wxPay.unifiedOrder(requestParam);
            //=====向mq写入订单查询的消息=====
            PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();
            //订单号
            paymentResponseDTO.setOutTradeNo(weChatBean.getOutTradeNo());
            //支付渠道参数
            paymentResponseDTO.setContent(wxConfigParam);
            //msg
            paymentResponseDTO.setMsg("WX_JSAPI");
            payProducer.payOrderNotice(paymentResponseDTO);

            //准备h5网页需要的数据
            jsapiPayParam = new HashMap<>();
            jsapiPayParam.put("appId",wxConfigParam.getAppId());
            jsapiPayParam.put("timeStamp",System.currentTimeMillis()/1000+"");
            jsapiPayParam.put("nonceStr", UUID.randomUUID().toString());//随机字符串
            jsapiPayParam.put("package","prepay_id="+resp.get("prepay_id"));
            jsapiPayParam.put("signType","HMAC-SHA256");
            //将h5网页响应给前端
            jsapiPayParam.put("paySign", WXPayUtil.generateSignature(jsapiPayParam,wxConfigParam.getKey(), WXPayConstants.SignType.HMACSHA256));
            return jsapiPayParam;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorCode.E_400001);
        }
    }

    //解析支付宝的订单状态为闪聚平台的状态
    private TradeStatus converAliTradeStatusToShanjuCode(String aliTradeStatus) {
        switch (aliTradeStatus){
            case AliCodeConstants.TRADE_FINISHED:
            case AliCodeConstants.TRADE_SUCCESS:
                return TradeStatus.SUCCESS;//业务交易支付 明确成功
            case AliCodeConstants.TRADE_CLOSED:
                return TradeStatus.REVOKED;//交易已撤销
            case AliCodeConstants.WAIT_BUYER_PAY:
                return TradeStatus.USERPAYING;//交易新建，等待支付
            default:
                return TradeStatus.FAILED;//交易失败
        }
    }
}
