package com.itheima.store.web.servlet;

import com.itheima.store.domain.Category;
import com.itheima.store.service.CategoryService;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.BeasServlet;
import com.itheima.store.utils.UUIDUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

/**
 * User: 李锦卓
 * Time: 2018/7/13 23:03
 */
@WebServlet(urlPatterns="/AdminCategoryServlet")
public class AdminCategoryServlet extends BeasServlet {
    //管理员查询所有分类
    public String findAll(HttpServletRequest request, HttpServletResponse response){
        try {
            CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
            List<Category> list = categoryService.findAll();
            request.setAttribute("list",list);
            return "/admin/category/list.jsp";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //跳转分类添加页面
    public String saveUI(HttpServletRequest request, HttpServletResponse response){
        return "/admin/category/add.jsp";
    }
    //添加分类
    public String save(HttpServletRequest request, HttpServletResponse response){
        try {
            String cname = request.getParameter("cname");
            Category category = new Category();
            category.setCid(UUIDUtils.getUUID());
            category.setCname(cname);
            CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
            categoryService.save(category);
            response.sendRedirect(request.getContextPath()+"/AdminCategoryServlet?method=findAll");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //删除分类
    public String delete (HttpServletRequest request, HttpServletResponse response){
        try {
            String cid = request.getParameter("cid");
            CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
            categoryService.delete(cid);
            response.sendRedirect(request.getContextPath()+"/AdminCategoryServlet?method=findAll");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //根据id查询分类
    public String edit(HttpServletRequest request, HttpServletResponse response){
        try {
            String cid = request.getParameter("cid");
            CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
            Category category = categoryService.findByCid(cid);
            request.setAttribute("category",category);
            return "/admin/category/edit.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //修改分类
    public String update(HttpServletRequest request, HttpServletResponse response){
        try {
            String cid = request.getParameter("cid");
            String name = request.getParameter("cname");
            CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
            categoryService.update(cid,name);
            response.sendRedirect(request.getContextPath()+"/AdminCategoryServlet?method=findAll");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}