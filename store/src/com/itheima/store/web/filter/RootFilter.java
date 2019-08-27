package com.itheima.store.web.filter;

import com.itheima.store.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public class RootFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            request.setAttribute("msg","您还没有登陆 请去登陆");
            request.getRequestDispatcher("/jsp/msg.jsp").forward(request,resp);
        }else {
            chain.doFilter(request, resp);
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
