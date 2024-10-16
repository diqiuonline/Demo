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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

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
                //先获取授权码，申请openid，再到支付确认页面
                return transactionService.getWXOAuth2Code(payOrderDTO);

            default:
        }

        //客户端的类型不支持  抓发到错误页面
        return "forward:/pay-page-error";
    }
    //支付宝的下单接口 将前端订单确认页面的参数请求进来


    /**
     * 授权码回调，申请获取授权码，微信将授权码请求到此地址
     * @param code 授权码
     * @param state 订单信息
     * @return
     */
    @ApiOperation("微信授权码回调")
    @GetMapping("/wx-oauth-code-return")
    public String wxOAuth2CodeReturn(@RequestParam String code, @RequestParam String state)  {

        String jsonString = EncryptUtil.decodeUTF8StringBase64(state);
        PayOrderDTO payOrderDTO = JSON.parseObject(jsonString, PayOrderDTO.class);
        //闪聚平台的应用id
        String appId = payOrderDTO.getAppId();

        //接收到code授权码，申请openid
        String openId = transactionService.getWXOAuthOpenId(code, appId);
        //将对象的属性和值组成一个url的key/value串
        String params = null;
        try {
            params = ParseURLPairUtil.parseURLPair(payOrderDTO);
            //转发到支付确认页面
            String url = String.format("forward:/pay-page?openId=%s&%s", openId, params);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return "forward:/pay-page-error";
        }


    }



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


    //微信下单 /wxjspay
    @ApiOperation("微信门店下单付款")
    @PostMapping("/wxjspay")
    public ModelAndView createWXOrderForStore(OrderConfirmVO orderConfirmVO, HttpServletRequest request){
        PayOrderDTO payOrderDTO = PayOrderConvert.INSTANCE.vo2dto(orderConfirmVO);
        //应用id
        String appId = payOrderDTO.getAppId();
        AppDTO app = appService.getAppById(appId);
        //商户id
        payOrderDTO.setMerchantId(app.getMerchantId());
        //客户端ip
        payOrderDTO.setClientIp(IPUtil.getIpAddr(request));
        //将前端输入的元转成分
        payOrderDTO.setTotalAmount(Integer.parseInt(AmountUtil.changeY2F(orderConfirmVO.getTotalAmount().toString())));
        //调用submitOrderByWechat
        Map<String, String> model = transactionService.submitOrderByWechat(payOrderDTO);
        return new ModelAndView("wxpay",model);

    }

}
