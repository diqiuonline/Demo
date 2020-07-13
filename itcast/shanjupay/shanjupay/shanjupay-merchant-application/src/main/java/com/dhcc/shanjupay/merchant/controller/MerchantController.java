package com.dhcc.shanjupay.merchant.controller;

import com.dhcc.shanjupay.merchant.common.util.SecurityUtil;
import com.dhcc.shanjupay.merchant.convert.MerchantConvert;
import com.dhcc.shanjupay.merchant.convert.MerchantDetailConvert;
import com.dhcc.shanjupay.merchant.service.FileService;
import com.dhcc.shanjupay.merchant.service.SmsService;
import com.dhcc.shanjupay.merchant.vo.MerchantDetailVO;
import com.dhcc.shanjupay.merchant.vo.MerchantRegisterVO;
import com.dhcc.shanjupay.merchant.api.MerchantService;
import com.dhcc.shanjupay.merchant.api.dto.MerchantDTO;
import com.dhcc.shanjupay.common.domain.BusinessException;
import com.dhcc.shanjupay.common.domain.CommonErrorCode;
import com.dhcc.shanjupay.common.util.PhoneUtil;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/9 20:14
 */
@RestController
@Api(value = "商户平台-应用接口", tags = "商户平台-应用接口", description = "商户平台应用接口")
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
        if (!PhoneUtil.isMatches(phone)) {
            throw new BusinessException(CommonErrorCode.E_100109);
        }
        //请求验证码服务
        String key = smsService.SendMsg(phone);
        return key;
    }

    @ApiOperation("商户注册")
    @ApiImplicitParam(value = "商户注册信息", name = "merchantRegisterVO", required = true, dataType = "MerchantRegisterVO", paramType = "body")
    @PostMapping("/merchants/register")
    public MerchantRegisterVO registerMerchant(@RequestBody MerchantRegisterVO merchantRegisterVO) {
        //校验参数的合法性
        if (StringUtils.isEmpty(merchantRegisterVO)) {
            throw new BusinessException(CommonErrorCode.E_100108);
        }
        if (StringUtils.isEmpty(merchantRegisterVO.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100108);
        }
        if (!PhoneUtil.isMatches(merchantRegisterVO.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100109);
        }


        //校验验证码
        smsService.checkVerifiyCode(merchantRegisterVO.getVerifiykey(), merchantRegisterVO.getVerifiyCode());
        //调用dubbo服务
        //想dto中写入商户西信息
        MerchantDTO merchantDTO = MerchantConvert.INSTANCE.voToDto(merchantRegisterVO);
        merchantService.createMerchant(merchantDTO);
        return merchantRegisterVO;
    }


    @Autowired
    FileService fileService;

    //上传证件照
    @ApiOperation("上传证件照")
    @PostMapping("/upload")
    public String upload(@ApiParam(value = "证件照", required = true) @RequestParam("file") MultipartFile multipartFile) throws IOException {
        //原始名称
        String originalFilename = multipartFile.getOriginalFilename();
        //扩展名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") - 1);
        //生成文件名
        String filename = UUID.randomUUID().toString() + suffix;
        String fileUrl = fileService.upload(multipartFile.getBytes(), filename);
        log.info(fileUrl);
        return fileUrl;
    }

    @ApiOperation("资质申请")
    @PostMapping("/my/merchants/save")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantInfo", value = "商户认证资料", required = true, dataType = "MerchantDetailVO", paramType = "body")
    })
    public void saveMerchant(@RequestBody MerchantDetailVO merchantDetailVO) {
        //取出当前登录用户的id  解析token
        Long merchantId = SecurityUtil.getMerchantId();
        System.out.println(merchantId);
        MerchantDTO merchantDTO = MerchantDetailConvert.INSTANCE.voToDto(merchantDetailVO);
        merchantService.applyMerchant(merchantId,merchantDTO
        );
    }
}
