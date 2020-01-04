package com.dhcc.servlet;

import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "loginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private static final String PASSWARD = "123456";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        Map resulMap = new HashMap();
        if (password != null && password.equals(PASSWARD)) {
            req.getSession().setAttribute("username", username);
            resulMap.put("success", true);
            resulMap.put("message", "登陆成功");
        } else {
            resulMap.put("success", false);
            resulMap.put("message", "登陆失败");
        }
        resp.getWriter().write(JSON.toJSONString(resulMap));
    }
}
