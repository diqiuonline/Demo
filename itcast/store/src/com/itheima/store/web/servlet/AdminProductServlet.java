package com.itheima.store.web.servlet;

import com.itheima.store.domain.Category;
import com.itheima.store.domain.PageBean;
import com.itheima.store.domain.Product;
import com.itheima.store.service.CategoryService;
import com.itheima.store.service.ProductService;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.BeasServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * User: 李锦卓
 * Time: 2018/7/15 21:26
 */
@WebServlet(urlPatterns="/AdminProductServlet")
public class AdminProductServlet extends BeasServlet {
    //查询商品
    public String findByPage(HttpServletRequest request, HttpServletResponse response){
        try {
            String currPage = request.getParameter("currPage");
            ProductService productService = (ProductService) BeanFactory.getBean("productService");
            PageBean<Product> pageBean = productService.findByPage(currPage);
            request.setAttribute("pageBean",pageBean);
            return "/admin/product/list.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //编辑商品
    public String updateProductByPid(HttpServletRequest request, HttpServletResponse response){
        try {
            String pid = request.getParameter("pid");
            ProductService productService = (ProductService) BeanFactory.getBean("productService");
            CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
            List<Category> list = categoryService.findAll();
            request.setAttribute("list",list);
            //Product product = productService.findBypid(pid);
            Product product = productService.findPCByPid(pid);
            request.setAttribute("product",product);
            return "/admin/product/edit.jsp";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //添加商品
    public String addProduct(HttpServletRequest request, HttpServletResponse response){
        try {
            CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
            List<Category> list = categoryService.findAll();
            request.setAttribute("list",list);
            return "/admin/product/add.jsp";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}