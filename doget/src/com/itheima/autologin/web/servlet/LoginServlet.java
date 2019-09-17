package com.itheima.autologin.web.servlet;

import com.itheima.autologin.domin.UserPojo;
import com.itheima.autologin.service.UserService;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


public class LoginServlet extends HttpServlet{
	@Override
	public void init() throws ServletException {
		int count = 0;
		this.getServletContext().setAttribute("count", count);
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		resp.setContentType("text/html;charset=UTF-8");
		//验证码校验程序
		/*String code = req.getParameter("code");
		String code2 = (String) req.getSession().getAttribute("code");
		req.getSession().removeAttribute("code");
		if(!code.equalsIgnoreCase(code2)){
			req.setAttribute("msg", "验证码错误");
			req.getRequestDispatcher("/login/login.jsp").forward(req, resp);
			return;
		}*/
		try {
			//接受表单数据
			Map<String, String[]> map = req.getParameterMap();
			//封装javabean
			UserPojo user = new UserPojo();
			BeanUtils.populate(user,map);
			//调用业务层处理数据
			
			UserService userservice = new UserService();
			UserPojo user2 = userservice.login(user);
			
			if(user2 != null){
				//自动登陆
				String autologin = req.getParameter("autologin");
				if ("true".equals(autologin)){
					//自动登陆已经选择
					Cookie cookie = new Cookie("autologin",user2.getUsername()+"#"+user2.getPassword());
					cookie.setPath("/day_12");
					cookie.setMaxAge(60*60*24*7);
					resp.addCookie(cookie);
				}

				//保存登陆信息
				this.getServletContext().setAttribute("userLogin1", user2);
				//跳转
				int count = (int) this.getServletContext().getAttribute("count");
				count++;
				this.getServletContext().setAttribute("count", count);
				
				//resp.getWriter().print("<h1>登陆成功  你好"+user2.getUsername()+"</h1>");
				//resp.getWriter().print("<h3>页面将在三秒后跳转</h3>");
				//resp.setHeader("Refresh", "3;url = /day_04/SkipServlet");
				resp.sendRedirect(req.getContextPath()+"/login/await.jsp");
				//req.setAttribute("name", "登陆成功");
				//req.getRequestDispatcher("/Login/index.html").forward(req, resp);
				
				//resp.sendRedirect(req.getContextPath()+"/Visit/index.html");
			}else{
				req.setAttribute("name", "登陆失败");
				req.getRequestDispatcher("/login/login.jsp").forward(req, resp);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
}
