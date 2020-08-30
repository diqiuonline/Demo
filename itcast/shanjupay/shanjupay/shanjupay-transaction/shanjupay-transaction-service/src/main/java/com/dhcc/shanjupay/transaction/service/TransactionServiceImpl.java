package com.dhcc.shanjupay.transaction.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dhcc.shanjupay.common.domain.BusinessException;
import com.dhcc.shanjupay.common.domain.CommonErrorCode;
import com.dhcc.shanjupay.common.util.AmountUtil;
import com.dhcc.shanjupay.common.util.EncryptUtil;
import com.dhcc.shanjupay.common.util.IdWorkerUtils;
import com.dhcc.shanjupay.common.util.PaymentUtil;
import com.dhcc.shanjupay.merchant.api.AppService;
import com.dhcc.shanjupay.merchant.api.MerchantService;
import com.dhcc.shanjupay.paymentagent.api.PayChannelAgentService;
import com.dhcc.shanjupay.paymentagent.api.conf.AliConfigParam;
import com.dhcc.shanjupay.paymentagent.api.dto.AlipayBean;
import com.dhcc.shanjupay.paymentagent.api.dto.PaymentResponseDTO;
import com.dhcc.shanjupay.transaction.api.PayChannelService;
import com.dhcc.shanjupay.transaction.api.TransactionService;
import com.dhcc.shanjupay.transaction.api.dto.PayChannelParamDTO;
import com.dhcc.shanjupay.transaction.api.dto.PayOrderDTO;
import com.dhcc.shanjupay.transaction.api.dto.QRCodeDto;
import com.dhcc.shanjupay.transaction.convert.PayOrderConvert;
import com.dhcc.shanjupay.transaction.entity.PayOrder;
import com.dhcc.shanjupay.transaction.mapper.PayOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.sql.PooledConnection;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/8/18 15:23
 */
@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    @Reference
    private AppService appService;
    @Reference
    private MerchantService merchantService;
    //从配置文件读取支付入口地址
    @Value("${shanjupay.payurl}")
    String payurl;
    @Autowired
    private PayOrderMapper payOrderMapper;
    @Reference
    private PayChannelAgentService payChannelAgentService;
    @Autowired
    private PayChannelService payChannelService;



    @Override
    public String createStoreQRCode(QRCodeDto qrCodeDto) throws BusinessException {
        //校验商户的id和应用id和门店id的合法性
        //校验应用是否输入商户
        Boolean aBoolean = appService.queryAppInMerchant(qrCodeDto.getAppId(), qrCodeDto.getMerchantId());
        if (!aBoolean) {
            throw new BusinessException(CommonErrorCode.E_200005);
        }
        //验门店是否属于商户
        Boolean aBoolean1 = merchantService.queryStoreInMerchant(qrCodeDto.getStoreId(), qrCodeDto.getMerchantId());
        if (!aBoolean1) {
            throw new BusinessException(CommonErrorCode.E_200006);
        }
        //组装url所需要的数据
        PayOrderDTO payOrderDTO = new PayOrderDTO();
        payOrderDTO.setMerchantId(qrCodeDto.getMerchantId());
        payOrderDTO.setAppId(qrCodeDto.getAppId());
        payOrderDTO.setStoreId(qrCodeDto.getStoreId());
        payOrderDTO.setSubject(qrCodeDto.getSubject());//显示订单标题
        payOrderDTO.setChannel("shanju_c2b");//服务类型，要写为c扫b的服务类型
        payOrderDTO.setBody(qrCodeDto.getBody());//订单内容
        //转json
        String jsonString = JSON.toJSONString(payOrderDTO);
        //用base64编码
        String ticket = EncryptUtil.encodeUTF8StringBase64(jsonString);

        String url = payurl + ticket;
        //目标是生成一个支付入口的url 需要携带参数将传入的参数转成json 用base64编码
        return url;
    }

    @Override
    public PaymentResponseDTO submitOrderByAli(PayOrderDTO payOrderDTO) throws BusinessException {
        //保存订单到闪聚支付平台数据库
        payOrderDTO.setChannel("ALIPAY_WAP"); //支付渠道
        PayOrderDTO save = save(payOrderDTO);
        //调用支付宝渠道代理服务 调用支付宝下单接口
        PaymentResponseDTO paymentResponseDTO = alipayH5(save.getTradeNo());

        return paymentResponseDTO;
    }


    //查询订单信息
    public PayOrderDTO queryPayOrder(String tradeNo) {
        PayOrder payOrder = payOrderMapper.selectOne(new LambdaQueryWrapper<PayOrder>().eq(PayOrder::getTradeNo, tradeNo));
        return PayOrderConvert.INSTANCE.entity2dto(payOrder);
    }


    //调用支付宝渠道代理服务 调用支付宝下单接口
    private PaymentResponseDTO alipayH5(String tradeNo) {
        //前端提交的订单信息 从数据库查询
        PayOrderDTO payOrderDTO = queryPayOrder(tradeNo);
        //组装alipayBean
        AlipayBean alipayBean = new AlipayBean();
        alipayBean.setOutTradeNo(payOrderDTO.getTradeNo()); //订单号
        try {
            alipayBean.setTotalAmount(AmountUtil.changeF2Y(payOrderDTO.getTotalAmount().toString()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorCode.E_300006);
        }
        alipayBean.setSubject(payOrderDTO.getSubject());
        alipayBean.setBody(payOrderDTO.getBody());
        alipayBean.setExpireTime("30m");
        /**
         * alipayTradeWapPayModel.setOutTradeNo(alipayBean.getOutTradeNo()); // 商户的订单，就是闪聚平台的订单
         *         alipayTradeWapPayModel.setTotalAmount(alipayBean.getTotalAmount()); //订单金额 元
         *         alipayTradeWapPayModel.setSubject(alipayBean.getSubject()); //订单标题
         *         alipayTradeWapPayModel.setBody(alipayBean.getBody()); //订单描述
         *         alipayTradeWapPayModel.setTimeExpire(alipayBean.getExpireTime()); //订单过期时间
         *         alipayTradeWapPayModel.setProductCode("QUICK_WAP_WAY");
         */
        //支付渠道配置参数 从数据库查询
        PayChannelParamDTO payChannelParamDTO = payChannelService.queryParamByAppPlatformAndPayChannel(payOrderDTO.getAppId(), "shanju_c2b", "ALIPAY_WAP");
        String paramJson = payChannelParamDTO.getParam();
        AliConfigParam aliConfigParam = JSON.parseObject(paramJson, AliConfigParam.class);
        aliConfigParam.setCharest("utf-8");
        PaymentResponseDTO paymentResponseDTO = payChannelAgentService.createPayOrderByAliWAP(aliConfigParam, alipayBean);
        return paymentResponseDTO;

    }

    //保存订单
    private PayOrderDTO save(PayOrderDTO payOrderDTO) {
        PayOrder payOrder = PayOrderConvert.INSTANCE.dto2entity(payOrderDTO);
        payOrder.setTradeNo(PaymentUtil.genUniquePayOrderNo()); //采用雪花算法订单id
        payOrder.setCreateTime(LocalDateTime.now()); //创建时间
        payOrder.setExpireTime(LocalDateTime.now().plus(30, ChronoUnit.MONTHS));//过期时间是30分钟之后
        payOrder.setCurrency("CNY"); //人名币
        payOrder.setTradeState("0"); //订单状态 0 生成订单
        payOrderMapper.insert(payOrder);
        return PayOrderConvert.INSTANCE.entity2dto(payOrder);
    }
}
