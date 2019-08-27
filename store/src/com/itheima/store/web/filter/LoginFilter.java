package com.itheima.store.web.filter;

import com.itheima.store.domain.User;
import com.itheima.store.service.Impl.UserServiceImpl;
import com.itheima.store.service.UserService;
import com.itheima.store.utils.CookieUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebFilter(filterName = "LoginFilter",urlPatterns = "/*")
public class LoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
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
        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user!=null){
            chain.doFilter(request, resp);
        }else {
            //session 中没有用户的信息
            Cookie[] cookies = request.getCookies();
            Cookie cookie = CookieUtils.findCookie(cookies, "autoLogin");
            if (cookie==null){
                //cookie为空
                chain.doFilter(request, resp);
            }else {
                //cookie带着信息过来
                String value = cookie.getValue();
                String username = value.split("#")[0];
                String password = value.split("#")[1];
                User user1 = new User();
                user1.setUsername(username);
                user1.setPassword(password);
                UserService userService = new UserServiceImpl();
                try {
                    User login = userService.login(user1);
                    if (login == null){
                        //cookie信息被修改了
                        chain.doFilter(request, resp);
                    }else {
                        session.setAttribute("user",login);
                        chain.doFilter(request, resp);
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
