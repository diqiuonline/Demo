package com.itheima.autologin.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Servlet implementation class CartServlet
 */
public class CartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//接收商品名称
		String name = request.getParameter("name");
		name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
		HttpSession session = request.getSession();
		Map<String,Integer>map = (Map<String, Integer>)session.getAttribute("cart");
		if(map == null){
			map = new LinkedHashMap<String,Integer>();
		}
		//判断是不是第一次加购物车
		if(map.containsKey(name)){
			Integer count = map.get(name);
			count++;
			map.put(name, count);
		}else{
			map.put(name, 1);
		}
		//将map集合存入到session中
		session.setAttribute("cart", map);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().println("<h3><a href = '/day_12/login/product_list.jsp'>继续购物</a> | <a href = '/day_12/login/cart.jsp'>去结算</a></h3>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
