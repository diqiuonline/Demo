package com.itheima.autologin.web.filter;

import com.itheima.autologin.domin.UserPojo;
import com.itheima.autologin.service.UserService;
import com.utils.CookiesUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebFilter(filterName = "LoginFilter" )
public class LoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        /**
         * 判断session中是否有用户的信息:
         * * session中如果有:放行.
         * * session中没有:
         *    * 从Cookie中获取:
         *        * Cookie中没有:放行.
         *        * Cookie中有:
         *            * 获取Cookie中存的用户名和密码到数据库查询.
         *                * 没有查询到:放行.
         *                * 查询到:将用户信息存入到session . 放行.
         */
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        UserPojo user = (UserPojo) session.getAttribute("userLogin2");
        if(user != null){
            chain.doFilter(request,response);
        }else {
            //session中没有用户信息
            //获得cookie数据
            Cookie[] cookies = req.getCookies();
            Cookie cookie = CookiesUtils.findCookie(cookies, "autologin");
            //判断cookie中有没有数据
            if ( cookie == null || cookie.getValue()=="" ){
                //没有携 带信息
                chain.doFilter(request,response);
            }else {
                //带着cookie信息过来
                String value = cookie.getValue();//bbb#123
                //获取用户名和密码
                String username = value.split("#")[0];
                String password = value.split("#")[1];
                //查询
                UserPojo user3 = new UserPojo();
                user3.setUsername(username);
                user3.setPassword(password);
                UserService userService = new UserService();
                try {
                    user = userService.login(user3);
                    if (user == null){
                        //cookie被篡改了
                        chain.doFilter(request,response);
                    }else {
                        //将用户存入到session中
                        session.setAttribute("userLogin2",user);
                        chain.doFilter(request,response);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
