package com.itheima.store.web.servlet;

import com.itheima.store.domain.Cart;
import com.itheima.store.domain.CartIntem;
import com.itheima.store.domain.Product;
import com.itheima.store.domain.User;
import com.itheima.store.service.ProductService;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.BeasServlet;
import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@WebServlet(urlPatterns="/CartServlet")
public class CartServlet  extends BeasServlet {
   //添加购物车
   public String addItems(HttpServletRequest request, HttpServletResponse response)  {
       try {
           //令牌校验
           String toke1 = request.getParameter("toke");
           String toke2 = (String) request.getSession().getAttribute("toke");
           request.getSession().removeAttribute("toke");
           if (!toke1.equals(toke2)) {
                request.setAttribute("msg","重复添加");
                return "/jsp/msg.jsp";
           }
           String pid = request.getParameter("pid");
           String count = request.getParameter("quantity");
           //获取商品对象
           ProductService productService = (ProductService) BeanFactory.getBean("productService");
           Product product = productService.findBypid(pid);
           //设置购物项
           CartIntem cartIntem = new CartIntem();
           cartIntem.setCount(Integer.parseInt(count));
           cartIntem.setProduct(product);
           //加入购物车
           Cart cart = (Cart) request.getSession().getAttribute("cart");
           if (cart == null){
               //第一次添加购物车
               cart = new Cart();
               cart.addCart(cartIntem);
               request.getSession().setAttribute("cart",cart);
           }else {
               cart.addCart(cartIntem);
               request.getSession().setAttribute("cart",cart);
           }
           response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
       } catch (Exception e) {
           e.printStackTrace();
       }

       return null;
   }
   //清空购物车
    public String clearCaet(HttpServletRequest request, HttpServletResponse response){
       Cart cart = (Cart) request.getSession().getAttribute("cart");
       cart.clear();
        //request.getSession().setAttribute("cart",cart);
       return "/";
    }
    //移除购物项
    public String removeCart(HttpServletRequest request, HttpServletResponse response){
       String pid = request.getParameter("pid");
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        cart.removeCart(pid);
        //request.getSession().setAttribute("cart",cart);
        return "/jsp/cart.jsp";
    }

}
