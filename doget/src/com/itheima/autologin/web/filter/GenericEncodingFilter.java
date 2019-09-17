package com.itheima.autologin.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "GenericEncodingFilter")
public class GenericEncodingFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        MyHttpServletRequestWrapper myreq = new MyHttpServletRequestWrapper(request);
        chain.doFilter(myreq, resp);
        System.out.println("214");
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
