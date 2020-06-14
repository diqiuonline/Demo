package com.dhcc.shanjupay.merchant.controller;

import com.dhcc.shanjupay.merchant.convert.MerchantConvert;
import com.dhcc.shanjupay.merchant.service.SmsService;
import com.dhcc.shanjupay.merchant.vo.MerchantRegisterVO;
import com.dhcc.shanjupay.merchat.api.MerchantService;
import com.dhcc.shanjupay.merchat.api.dto.MerchantDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/9 20:14
 */
@RestController
@Api(value = "商户平台应用接口", tags = "商户平台应用接口", description = "商户平台应用接口")
public class MerchantController {
    public static final Logger log = LoggerFactory.getLogger(MerchantController.class);
    @Reference
    private MerchantService merchantService;

    @ApiOperation(value = "根据id查询商户信息")
    @GetMapping("/merchants/{id}")
    public MerchantDTO queryMerchantById(@PathVariable("id") Long id) {
        MerchantDTO merchantDTO = merchantService.queryMerchantById(id);
        log.info(merchantDTO.toString());
        return merchantDTO;
    }

    @Autowired
    private SmsService smsService;

    @ApiOperation("获取手机验证码")
    @GetMapping("/sms")
    @ApiImplicitParam(value = "手机号", name = "phone", required = true, dataType = "string", paramType = "query")
    public String getSMSCode(@RequestParam("phone") String phone) {
        //请求验证码服务
        String key = smsService.SendMsg(phone);
        return key;
    }

    @ApiOperation("商户注册")
    @ApiImplicitParam(value = "商户注册信息", name = "merchantRegisterVO", required = true, dataType = "MerchantRegisterVO", paramType = "body")
    @PostMapping("/merchants/register")
    public MerchantRegisterVO registerMerchant(@RequestBody MerchantRegisterVO merchantRegisterVO) {
        //校验验证码
        smsService.checkVerifiyCode(merchantRegisterVO.getVerifiykey(),merchantRegisterVO.getVerifiyCode());
        //调用dubbo服务
        //想dto中写入商户西信息
        MerchantDTO merchantDTO = MerchantConvert.INSTANCE.voToDto(merchantRegisterVO);
        merchantService.createMerchant(merchantDTO);
        return null;
    }
}
