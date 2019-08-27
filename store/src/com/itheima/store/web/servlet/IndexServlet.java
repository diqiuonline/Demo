package com.itheima.store.web.servlet;

import com.itheima.store.domain.Product;
import com.itheima.store.service.ProductService;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.BeasServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns="/IndexServlet")
public class IndexServlet extends BeasServlet {
    public String index(HttpServletRequest request,HttpServletResponse response) {
        //显示最新与热门商品
        try {
            ProductService productService = (ProductService) BeanFactory.getBean("productService");
            List<Product> hotlist = productService.findByHot();
            List<Product> newlist = productService.findByNew();
            request.setAttribute("hotlist",hotlist);
            request.setAttribute("newlist",newlist);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "/jsp/index.jsp";
    }
}
