package com.itheima.autologin.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "ClearServletLogin",urlPatterns = "/ClearServletLogin")
public class ClearServletLogin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("userLogin2");

        Cookie cookie = new Cookie("autologin", "");
        //Cookie[] cookies = request.getCookies();
        //Cookie cookie = CookiesUtils.findCookie(cookies, "autologin");
       //cookie.setPath("/day_12");
        //cookie.setMaxAge(0);
        response.addCookie(cookie);

        response.sendRedirect(request.getContextPath()+"/login/login.jsp");
       // response.sendRedirect(request.getContextPath()+"/login/index.jsp");
    }
}
