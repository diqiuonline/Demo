package com.itheima.store.web.servlet;

import com.itheima.store.domain.User;
import com.itheima.store.service.Impl.UserServiceImpl;
import com.itheima.store.service.UserService;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.BeasServlet;
import com.itheima.store.utils.MyDateConveter;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

@WebServlet(urlPatterns="/UserServlet")
public class UserServlet extends BeasServlet {
    //异步校验用户名是否存在
    public String checkUsername(HttpServletRequest request, HttpServletResponse response){
        try {
            //获取数据
            String username = request.getParameter("username");
            if (username!=""){
                //调用service方法校验是否存在 返回一个user对象
                UserService userService = (UserService) BeanFactory.getBean("userService");
                User user = userService.findByUsername(username);
                //System.out.println(user.toString());
                if (user!=null){
                    response.getWriter().println("1");
                }else {
                    response.getWriter().println("2");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //跳转登陆页面
    public String loginUI(HttpServletRequest request, HttpServletResponse response){
        return "/jsp/login.jsp";
    }
    //跳转注册页面
    public String registerUI(HttpServletRequest request, HttpServletResponse response){
        return "/jsp/register.jsp";
    }
    //注册页面
    public String register(HttpServletRequest request, HttpServletResponse response){
        try {
            //封装数据
            Map<String, String[]> map = request.getParameterMap();
            User user = new User();
            ConvertUtils.register(new MyDateConveter(),Date.class);
            BeanUtils.populate(user,map);
            //调用业务层
            UserService userService = (UserService) BeanFactory.getBean("userService");
            userService.save(user);
            //页面跳转
            request.setAttribute("msg","注册成功 请去邮箱激活");
            return "/jsp/msg.jsp";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/jsp/register.jsp";
    }
    //激活用户()
    public String active(HttpServletRequest request, HttpServletResponse response){
        try {
            String code = request.getParameter("code");
            UserService userService = (UserService) BeanFactory.getBean("userService");
            User user = userService.findVBycode(code);
            if (user==null){
                request.setAttribute("msg","激活码有误");
                return "/jsp/msg.jsp";
            }else {
                user.setState(0);
                user.setCode(null);
                userService.update(user);
                request.setAttribute("msg","激活成功 请去登陆");
                return "/jsp/msg.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //登陆页面
    public String login(HttpServletRequest request, HttpServletResponse response){
        try {

            //一次性验证码
            String code1 = request.getParameter("code");
            String code2 = (String) request.getSession().getAttribute("code");
            request.getSession().removeAttribute("code");
            if(!code2.equalsIgnoreCase(code1)){
                request.setAttribute("msg","验证码错误");
                return "/jsp/login.jsp";
            }

            Map<String, String[]> map = request.getParameterMap();
            User user = new User();
            BeanUtils.populate(user,map);
            UserService userService = (UserService) BeanFactory.getBean("userService");
            User existUser = userService.login(user);
            if (existUser==null){
                request.setAttribute("msg","用户名或密码错误或账户未激活");
                return  "/jsp/msg.jsp";
            }else{
                //登陆成功 自动登陆
                String autoLogin = request.getParameter("autoLogin");
                if("true".equals(autoLogin)){
                    Cookie cookie = new Cookie("autoLogin",existUser.getUsername()+"#"+existUser.getPassword());
                    cookie.setMaxAge(7*24*60*60);
                    response.addCookie(cookie);
                }
                //记住用户名
                String remember = request.getParameter("remember");
                if ("true".equals(remember)){
                    Cookie cookie = new Cookie("username",existUser.getUsername());
                    cookie.setMaxAge(24*60*60);
                    response.addCookie(cookie);
                }


                request.getSession().setAttribute("user",existUser);
                response.sendRedirect(request.getContextPath()+"/index.jsp");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //账户退出
    public String logOut(HttpServletRequest request, HttpServletResponse response){
        try {
            request.getSession().invalidate();
            Cookie cookie = new Cookie("autoLogin","") ;
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            response.sendRedirect(request.getContextPath()+"/index.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
