package com.itheima.store.web.servlet;

import com.itheima.store.domain.Category;
import com.itheima.store.service.CategoryService;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.BeasServlet;
import net.sf.json.JSONArray;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
@WebServlet(urlPatterns="/CategoryServlet")
public class CategoryServlet extends BeasServlet {
    //查询所有分类
    public String findAll(HttpServletRequest request, HttpServletResponse response){
        try {
            CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
            List<Category> list = categoryService.findAll();
            JSONArray array = JSONArray.fromObject(list);
            response.getWriter().println(array.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
