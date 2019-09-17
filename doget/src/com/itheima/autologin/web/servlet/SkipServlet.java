package com.itheima.autologin.web.servlet;
/**
 * * 判断用户是否是第一次访问:(从数组中没有找到指定名称的Cookie)
		 * * 如果是第一次:显示欢迎,记录当前访问的时间存入到Cookie中.
		 * * 如果不是第一次:显示欢迎,上一次访问时间,同时记录当前访问的时间存入到Cookie中。
		 *
		// 获得浏览器带过来的所有的Cookie:
 */

import com.utils.CookiesUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Servlet implementation class SkipServlet
 */
public class SkipServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		int count = (int) this.getServletContext().getAttribute("count");
		//response.getWriter().print("您是第"+count+"位登陆的顾客");
		//获得Cookies数组
		Cookie[] cookies = request.getCookies();
		//显示上次登陆时间
		Cookie lastCookie = CookiesUtils.findCookie(cookies, "lastCookie");
		//判断是否是第一次
		if(lastCookie != null){
			//不是第一次
			response.getWriter().println("您是第"+count+"位登陆的顾客");
			response.getWriter().println("您上次登陆的时间是"+lastCookie.getValue());
			response.setStatus(302);
			response.setHeader("Refresh", "3;url = http://localhost:8080/"+request.getContextPath()+"/ListServlet");
			
			
		}else{
			//是第一次
			response.getWriter().print("您是第"+count+"位登陆的顾客");
			//response.setHeader("Refresh", "3;url = /day_04/SkipServlet");
			response.setStatus(302);
			response.setHeader("Refresh", "3;url = http://localhost:8080/"+request.getContextPath()+"/ListServlet");
			
		}
		//保存本次登陆时间
		String data = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
		Cookie c = new Cookie("lastCookie",data);
		response.addCookie(c);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
