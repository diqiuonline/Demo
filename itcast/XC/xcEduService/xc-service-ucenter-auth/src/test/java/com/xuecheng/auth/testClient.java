package com.xuecheng.auth;

import com.xuecheng.framework.client.XcServiceList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * User: 李锦卓
 * Time: 2019/5/18 22:17
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class testClient {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    LoadBalancerClient loadBalancerClient;

    //远程请求springsecrurity 获取令牌
    @Test
    public void testClient() {
        //从eureka中获取认证服务地址
        //获取实例
        ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        //获取http地址
        URI uri = serviceInstance.getUri();
        //令牌申请的地址 http://localhost:40400/auth/oauth/token
        String authUrl = uri+"/auth/oauth/token";
        //定义header
        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        String httpBase = getHttpBasic("XcWebApp", "XcWebApp");
        header.add("Authorization", httpBase);
        //定义body
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username","XcWebApp");
        body.add("password","12334");

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, header);
        //设置resttemplate远程调用的时候 对400和401不报错 正确返回数据
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });
        //String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables
        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity, Map.class);
        //申请令牌信息
        Map body1 = exchange.getBody();
        System.out.println(body1);
    }

    private String getHttpBasic(String clientId,String clientSecret) {
        String string = clientId+":"+clientSecret;
        //将串进行base64编码
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic "+new String(encode);
    }
}