package com.dhcc.shanjupay.transaction.controller;

import com.alibaba.fastjson.JSON;
import com.dhcc.shanjupay.common.domain.BusinessException;
import com.dhcc.shanjupay.common.util.AmountUtil;
import com.dhcc.shanjupay.common.util.EncryptUtil;
import com.dhcc.shanjupay.common.util.IPUtil;
import com.dhcc.shanjupay.common.util.ParseURLPairUtil;
import com.dhcc.shanjupay.merchant.api.AppService;
import com.dhcc.shanjupay.merchant.api.dto.AppDTO;
import com.dhcc.shanjupay.paymentagent.api.dto.PaymentResponseDTO;
import com.dhcc.shanjupay.transaction.api.TransactionService;
import com.dhcc.shanjupay.transaction.api.dto.PayOrderDTO;
import com.dhcc.shanjupay.transaction.convert.PayOrderConvert;
import com.dhcc.shanjupay.transaction.vo.OrderConfirmVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 支付相关的接口
 *
 * @author 李锦卓
 * @version 1.0
 * @date 2020/8/22 15:21
 */
@Controller
@Slf4j
public class PayController {
    @Autowired
    private TransactionService transactionService;
    @Reference
    private AppService appService;
    /**
     * 支付入口
     *
     * @param ticket  传入数据，对json数据进行的base64编码
     * @param request
     * @return
     */
    @RequestMapping("/pay-entry/{ticket}")
    public String payEntry(@PathVariable("ticket") String ticket, HttpServletRequest request) throws Exception {
        //准备确认页面所需要数据
        String jsonString = EncryptUtil.decodeUTF8StringBase64(ticket);
        PayOrderDTO payOrderDTO = JSON.parseObject(jsonString, PayOrderDTO.class);
        //将对象的属性和值组成一个url的key/value串
        String s = ParseURLPairUtil.parseURLPair(payOrderDTO);
        // 解析客户端类型
        BrowserType browserType = BrowserType.valueOfUserAgent(request.getHeader("user-agent"));
        switch (browserType) {
            case ALIPAY:
                //转发到确认页面
                return "forward:/pay-page?"+s;
            case WECHAT:
                return "forward:/pay-page?"+s;

            default:
        }

        //客户端的类型不支持  抓发到错误页面
        return "forward:/pay-page-error";
    }
    //支付宝的下单接口 将前端订单确认页面的参数请求进来

    /**
     * 支付宝的下单接口,前端订单确认页面，点击确认支付，请求进来
     *
     * @param orderConfirmVO 订单信息
     * @param request
     * @param response
     */
    @ApiOperation("支付宝门店下单付款")
    @PostMapping("/createAliPayOrder")
    public void createAlipayOrderForStore(OrderConfirmVO orderConfirmVO, HttpServletRequest request, HttpServletResponse response) throws BusinessException,IOException {
        PayOrderDTO payOrderDTO = PayOrderConvert.INSTANCE.vo2dto(orderConfirmVO);
        String appId = payOrderDTO.getAppId();
        AppDTO appDTO = appService.getAppById(appId);
        payOrderDTO.setMerchantId(appDTO.getMerchantId()); //商户id
        payOrderDTO.setTotalAmount(Integer.parseInt(AmountUtil.changeY2F(orderConfirmVO.getTotalAmount().toString())));//前端输入的元转成分
        payOrderDTO.setClientIp(IPUtil.getIpAddr(request)); //客户端ip

        //保存订单
        //调用支付渠道代理服务 来第三方支付宝的下单接口
        PaymentResponseDTO<String> paymentResponseDTO = transactionService.submitOrderByAli(payOrderDTO);

        //向前端相应html页面
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(paymentResponseDTO.getContent());
        response.getWriter().flush();
        response.getWriter().close();
    }

}
