package com.dhcc.shanjupay.merchant.service;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/6/14 22:24
 */
@Service
@Slf4j
public class SmsServiceImpl implements SmsService {
    public static final Logger log = LoggerFactory.getLogger(SmsServiceImpl.class);
    @Autowired
    private RestTemplate restTemplate;

    @Value("${sms.url}")
    private String url;

    @Value("${sms.effectiveTime}")
    private String effectiveTime;

    @Override
    public String SendMsg(String phone) {

        String sms_url = url + "/generate?name=sms&effectiveTime=" + effectiveTime;
        //请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        //请求体
        Map<String, Object> body = new HashMap<>();
        body.put("mobile", phone);
        //请求信息
        HttpEntity httpEntity = new HttpEntity(body, httpHeaders);

        Map resultBody = null;
        try {
            ResponseEntity<Map> result = restTemplate.exchange(sms_url, HttpMethod.POST, httpEntity, Map.class);
            resultBody = result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取验证码失败");
        }
        if (StringUtils.isEmpty(resultBody) || StringUtils.isEmpty(resultBody.get("result"))) {
            throw new RuntimeException("获取验证码失败");
        }
        Map resultKey = (Map) resultBody.get("result");
        String key = (String) resultKey.get("key");
        log.info(key);
        return key;
    }

    @Override
    public void checkVerifiyCode(String verifiyKey, String verifiyCode)   {
        //定义校验验证码的url
        String sms_verifiy_url = url+"/verify?name=sms&verificationCode="+verifiyCode+"&verificationKey="+verifiyKey;
        //使用resttempllate 请求校验验证码服务
        Map resultBody = null;
        try {
            ResponseEntity<Map> result = restTemplate.exchange(sms_verifiy_url, HttpMethod.POST, HttpEntity.EMPTY, Map.class);
            resultBody = result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            //throw new RuntimeException("验证码校验失败");
            throw new BusinessException(CommonErrorCode.E_100102);
        }
        //int i = 1/0;
        if (StringUtils.isEmpty(resultBody) || StringUtils.isEmpty(resultBody.get("result")) || !(Boolean)resultBody.get("result")) {
            throw new BusinessException(CommonErrorCode.E_100102);
        }


    }
}
