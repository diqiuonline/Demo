package com.itheima.store.utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

public class BeasServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取客户端传递过来的参数
            req.setCharacterEncoding("UTF-8");
            resp.setContentType("text/html;charset = UTF-8");
            String methodName = req.getParameter("method");
            if (methodName == null ||   "".equals(methodName)){
                resp.getWriter().println("method参数错误");
            }
            //获取子类的class对象
            Class clazz = this.getClass();
            try {
                //获取method
                Method m = clazz.getMethod(methodName,HttpServletRequest.class,HttpServletResponse.class);
                //反射调用
                String path = (String) m.invoke(this,req,resp);
                if(path!=null){
                    req.getRequestDispatcher(path).forward(req,resp);
                }
            } catch (Exception e) {
                e.printStackTrace();
        }
    }
}
