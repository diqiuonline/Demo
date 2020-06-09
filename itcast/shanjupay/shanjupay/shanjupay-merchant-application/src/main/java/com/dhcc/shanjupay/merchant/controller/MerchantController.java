package com.dhcc.shanjupay.merchant.controller;

import com.dhcc.shanjupay.merchat.api.MerchantService;
import com.dhcc.shanjupay.merchat.api.dto.MerchantDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/9 20:14
 */
@RestController
@Api(value="商户平台应用接口",tags = "商户平台应用接口",description = "商户平台应用接口")
public class MerchantController {
    public static final Logger log = LoggerFactory.getLogger(MerchantController.class);
    @Reference
    private MerchantService merchantService;

    @ApiOperation(value="根据id查询商户信息")
    @GetMapping("/merchants/{id}")
    public MerchantDTO queryMerchantById(@PathVariable("id") Long id) {
        MerchantDTO merchantDTO = merchantService.queryMerchantById(id);
        log.info(merchantDTO.toString());
        return merchantDTO;
    }
}
