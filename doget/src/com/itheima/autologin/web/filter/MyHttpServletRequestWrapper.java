package com.itheima.autologin.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.UnsupportedEncodingException;

public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private HttpServletRequest request;
    public MyHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }
    @Override
    public String getParameter(String name){
        String method = request.getMethod();
        if ("get".equalsIgnoreCase(method)){
            String parameter = null;
            try {
                parameter = new String(request.getParameter(name).getBytes("ISO-8859-1"),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return  parameter;
        }else if ("post".equalsIgnoreCase(method)){
            try {
                request.setCharacterEncoding("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return super.getParameter(name);
    }
}
