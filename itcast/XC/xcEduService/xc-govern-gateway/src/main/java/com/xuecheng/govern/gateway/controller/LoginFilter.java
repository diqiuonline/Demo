/*
package com.xuecheng.govern.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.govern.gateway.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

*/
/**
 * User: 李锦卓
 * Time: 2019/5/23 22:24
 *//*

@Component
public class LoginFilter extends ZuulFilter {
    @Autowired
    AuthService authService;
    @Override
    public String filterType() {
        */
/**
         pre：请求在被路由之前执行

         routing：在路由请求时调用

         post：在routing和errror过滤器之后调用

         error：处理请求时发生错误调用

         *//*

        return "pre";
    }
    //过虑器序号，越小越被优先执行
    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //返回true表示要执行此过虑器
        return true;
    }

    //过虑器的内容
    //测试的需求：过虑所有请求，判断头部信息是否有Authorization，如果没有则拒绝访问，否则转发到微服务。
    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        //得到httpservletrequest
        HttpServletRequest request = requestContext.getRequest();
        //得到httpServletResponse
        HttpServletResponse response = requestContext.getResponse();
        //取出cookie中的身份令牌
        String tokenFromCookie = authService.tokenFormCookie(request);
        if(StringUtils.isEmpty(tokenFromCookie)){
            //拒绝访问
            this.access_denied();
            return null;
        }
        //从header中取出jwt
        String jwtFormHeader = authService.getJwtFormHeader(request);
        if (StringUtils.isEmpty(jwtFormHeader)) {
            this.access_denied();
            return null;
        }
        //从redis中取出过期时间
        long expire = authService.getExpire(tokenFromCookie);
        if (expire < 0) {
            this.access_denied();
            return null;
        }
        return null;
    }

    private void access_denied() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        //得到response
        HttpServletResponse response = requestContext.getResponse();
        //拒绝访问
        requestContext.setSendZuulResponse(false);
        //设置响应代码
        requestContext.setResponseStatusCode(200);
        //构建响应的信息
        ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
        //转成json
        String result = JSON.toJSONString(responseResult);
        requestContext.setResponseBody(result);
        //转成json，设置contentType
        response.setContentType("application/json;charset=utf-8");
    }
}*/
