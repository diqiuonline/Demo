package com.dhcc.shanjupay.paymentagent.service;

import com.dhcc.shanjupay.paymentagent.api.PayChannelAgentService;
import com.dhcc.shanjupay.paymentagent.api.conf.AliConfigParam;
import com.dhcc.shanjupay.paymentagent.api.dto.PaymentResponseDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/9/12 16:51
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestPayChannelAgentServiceImpl {
    @Autowired
    private PayChannelAgentService payChannelAgentService;
    @Test
    public void  testqueryPayOrderByAli() {
        //应用id
        String APP_ID = "2016102500760927";
        //应用私钥
        String APP_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCRa8APh9tzk94s9sufCaB2anHUeq6CMA46AukVMrxGr7SOzoxPzPOqSE3UcVCIw8th44lKbUx3jgeyK0tGGmkl9OQSNjAn5NLo/2Q9giPIneUSTHys9/6drVlFCkJsTJfALuS1ym5Q3VUD+GRz9LViyeO9pT4wychF22JRoJPJ4gqPXw9jbEDZ/MVqz3SKY5pbCvcSgl/ADEisUyirGLmF/v2W64VvCiVGCVsVvVo8OvUnwxhYuDKqBFouM0DqveNGv/NFqVzzOynjVYBnYluQ/nTdZOWyDMxOrCcumM9ksAp1H+EJuIKD3J/6GwcJDJih+NpLPNRH4xdBfeWNXRTJAgMBAAECggEAc+3oYPk3k74wAryEu7aa+4wETxW2hQb2cHzdgA7AVcHBOY+kAAItxnR5x4DGaSFdxB321ySnLzBFJl6pVau8Y4wFPheA8GzVTvY63WYgTNDDS4E6a1IQ43bMkeOJ7HhrCcTYg6xroX+xRMcCngZZi/XS3t8a4x4Z0McCbHyNFQUl5vMTcqHD4iTOSJ8YuI7JrO19tbXOq2rNYWLQL3q66karHBUybSEMFcEjBD6F/SCC6CDDl8W09ODJqRaW/DYSmrifR9GLNGc7OQRYqKMslW2161O0Vlq/jLIkk3JXpAXlBDcxmM9YvsXIKlzcXf90ZPRGimIsGAT9bnTrY96LAQKBgQD8wRKbscTlRPL0XH4lXeqS3WEyAc8BdEVnfwIP5wxi7pnQVUBX3rycNp/ZeQ8SyaLNVTRQQ//SKXV7tPBF4qjl36pr8R6zhfC3ZCAMNbmEXOVJgcVUrAZfoTKGSkl+77uNw4DLD3ius6LhRJ9COpuHTejelXtwEqDa+wPsG9DGmQKBgQCTSdH7YiHcyHldlngcINfDQZ02j4CnDbAe3aS9da8/ggiha2oOTP2ctHHIiUKE+PJzYDIdt/z7U4T8+yWZtzf9n8ocArMUuaEKQVrIaxtTXSr1jUzgowMw6VLuw1Q6e+LBEQL3XOs32V2+q0Rd8Y4hx7xkuyvVnr+Jzd5iQh4NsQKBgB1uIcuNPH7xs1RuvKV/NQQa0OlwunjR4A74A/Z0EcYnQDO46YARyGZsGdrJtM6cOd+szyWVdWE0QKDB5cRNaxsxGvfsoIWI0Amal/yz+MEHzA7uFFsSmL9j1q06Uyp7oZwVKpb6WzQEaK/QPLcKefecQ8Q/JZtcH+y/k6pXZszpAoGAREq/HPLkRo2BkH47FLe5syQSNAEXot164uTcBKizaBvvyoCWfSj/kpAsDdJu0F7pc8uPkS4FE7cm+3GrfnlwcZ2MRa6OwDZN0AZ7hF/9HIsfZxlsIuASk7Qrqt9Qh4l3mxM7L9yah5u2lrisi257+3e0B3Stn2/q2Ydl7GNBshECgYAKJ43AyilPfg4JmorJj2cUwzxDtM37VfxazzDWSuqT5hYrUTpw84pOykb2IlvI2aVyM7SHNDGh5vtACihkyTEuLl7yXBBW9uywUbBuNo8VC0WwMVthS8iKkzqVmtAig21ysgQXGwLan/dIlT5BqISsBvkDIgitGIlzmP8Vo15XJA==";
        //支付宝公钥
        String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwP4LmLXzzAHwKxtflfJLX5ptleOcC9RP4p6ygHqI4lBaHBqlVrZGefxyduWh4TkjKXem1EHcG+ehHrpEuFOEm22S8Ge3ETvCYTZ1Ngb9Py9SVqhiP0yDkOcgrhLYjugDsUNZ+G6VKFjoMdSsRghbfNV5S33vzGOkEeODsNeelSq+DE11xlil3rXXa5RhPj6mREqDoa0yzoKI7CQ0tMkUxMd3eHQpQ1ul54e0yDz4ZTwEGgnJcH9UQJVwnerhyS4be0nzsDHEwNFmPW24M+eiIF+qCzQ6Jen96G45IDNbRK6/ftiX1PFaC3/L9qqpwmgA0qqSwXwVeSch+jCtGF8dgwIDAQAB";
        String CHARSET = "utf-8";
        //支付宝接口的网关地址，正式"https://openapi.alipay.com/gateway.do"
        String serverUrl = "https://openapi.alipaydev.com/gateway.do";
        //签名算法类型
        String sign_type = "RSA2";
        //AliConfigParam aliConfigParam,String outTradeNo
        AliConfigParam aliConfigParam = new AliConfigParam();
        aliConfigParam.setUrl(serverUrl);
        aliConfigParam.setCharest(CHARSET);
        aliConfigParam.setAlipayPublicKey(ALIPAY_PUBLIC_KEY);
        aliConfigParam.setRsaPrivateKey(APP_PRIVATE_KEY);
        aliConfigParam.setAppId(APP_ID);
        aliConfigParam.setFormat("json");
        aliConfigParam.setSigntype("RSA2");

        //AliConfigParam aliConfigParam,String outTradeNo
        PaymentResponseDTO paymentResponseDTO = payChannelAgentService.queryPayOrderByAli(aliConfigParam, "SJ1304747973345873920");
        System.out.println(paymentResponseDTO);
    }
}
