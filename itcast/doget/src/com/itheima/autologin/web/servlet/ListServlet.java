package com.itheima.autologin.web.servlet;
/**
 * 接收商品id.
		   接收从客户端带过来的所有Cookie.
		   从Cookie的数组中查找指定名称的Cookie.
		   判断是否是第一次浏览商品:
		       第一次浏览商品
		          直接将商品的ID存入到Cookie.
		          将Cookie回写到浏览器.
		       不是第一次浏览商品 1-2
		          判断当前的商品是否已经在浏览记录.
		              已经存在: 2-1 移除当前元素，将当前元素添加到最开始.
		              没在浏览记录中: 
		                  判断是否已经超过了最大长度:如果超过 2-1-3:删除最后一个 将当前元素添加到最前面.
		                  没有超过:直接将该元素添加到最前位置.
		          将转换的id的值存入到Cookie,回写浏览器.
 */
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class ListServlet
 */
public class ListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print("<a href = 'http://localhost:8080/day_12/login/product_list.jsp'>商品列表</a>");
		//response.getWriter().println("<h3><a href='/day11/demo2/product_list.jsp'>商品列表</a></h3>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
