package com.itheima.autologin.web.servlet;
/**
 * 步骤一】：在登录完成后，显示商品列表页面.
【步骤二】：为商品列表页面做一些准备工作.
【步骤三】：点击某个商品,将商品ID传递一个Servlet.
【步骤四】：在Servlet中:判断是否是第一次浏览商品
【步骤五】：如果是第一次：将商品的ID存入到Cookie中即可.
【步骤六】：如果不是第一次：判断该商品是否已经浏览了.
【步骤七】：如果浏览器过.删除之前元素,将该元素添加到最前面.
【步骤八】：如果没有浏览过该商品.判断最大长度，没有超过限制,直接加到最前,如果已经超过限制,删除最后一个，将其插入到最前.
 */

import com.utils.CookiesUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;


/**
 * Servlet implementation class ProductServlet
 */
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//接收商品的ID
		String id = request.getParameter("id");
		//获得所有的Cookie
		Cookie[] cookies = request.getCookies();
		//判断是否是第一次
		Cookie cookie = CookiesUtils.findCookie(cookies, "history");
		if(cookie == null){
			//第一次浏览商品
			Cookie c = new Cookie("history", id);
			c.setPath("/day_12");
			c.setMaxAge(60*60*24);
			response.addCookie(c);
		}else{
			//不是第一次浏览
			String value = cookie.getValue();
			String[] sts = value.split("-");
			//将数据转换为集合
			LinkedList<String>list = new LinkedList<String>(Arrays.asList(sts));
			if(list.contains(id)){
				//之前浏览过该商品
				list.remove(id);
				list.addFirst(id);
			}else{
				//从没有浏览过该商品
				//判断商品数量
				if(list.size()>=3){
					//超过3个
					list.removeLast();
					list.addFirst(id);
				}else{
					//没有超过3个
					list.addFirst(id);
				}
			}
			//将list集合中的元素取出
			StringBuffer sb = new StringBuffer();
			for (String string : list) {
				sb.append(string).append("-");
			}
			String sValue = sb.toString().substring(0, sb.length()-1);
			System.out.println(sValue);
			//存入Cookie中
			Cookie c = new Cookie( "history",sValue);
			c.setPath("/day_12");
			c.setMaxAge(60*60*24);
			response.addCookie(c);
			
		}
		response.sendRedirect(request.getContextPath()+"/login/product_list.jsp");
		//request.getRequestDispatcher("/Cart/product_list.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
