package com.itheima.autologin.web.servlet;

import com.itheima.autologin.domin.UserPojo;
import com.itheima.autologin.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class LoginServlet
 */
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		try {
			
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			UserPojo user = new UserPojo();
			user.setPassword(password);
			user.setUsername(username);
			
			UserService service = new UserService();
			service.register(user);
			
			response.sendRedirect(request.getContextPath()+"/Cart/login.jsp");
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
