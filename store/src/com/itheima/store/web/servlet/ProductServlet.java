package com.itheima.store.web.servlet;

import com.itheima.store.domain.PageBean;
import com.itheima.store.domain.Product;
import com.itheima.store.service.Impl.ProductServiceImpl;
import com.itheima.store.service.ProductService;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.BeasServlet;
import com.itheima.store.utils.CookieUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;


@WebServlet(name = "ProductServlet",urlPatterns = "/ProductServlet")
public class ProductServlet extends BeasServlet {
    //查询分类数据
    public java.lang.String findByCid(HttpServletRequest request, HttpServletResponse response) {
        try {
            //获取当前页
            String currPage = request.getParameter("currPage");
            //获取分类信息
            String cid = request.getParameter("cid");

            // 调用业务层:
            ProductService productService = (ProductService) BeanFactory.getBean("productService");
            PageBean<Product> pageBean = productService.findByPageCid(cid,currPage);
            request.setAttribute("pageBean",pageBean);
            return "/jsp/product_list.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //查询商品详情
    public String findByPid(HttpServletRequest request,HttpServletResponse response) {
        try {
            String pid = request.getParameter("pid");
            ProductService productService = (ProductService) BeanFactory.getBean("productService");
            Product product = productService.findBypid(pid);
            request.setAttribute("p",product);

            //历史纪录
            Cookie[] cookies = request.getCookies();
            Cookie cookie = CookieUtils.findCookie(cookies, "history");
            //判断cookie中有没有
            if (cookie==null){
                //没有历史记录
                Cookie c = new Cookie("history",pid);
                c.setMaxAge(7*24*60*60);
                response.addCookie(c);
            }else {
                //有浏览记录
                String value = cookie.getValue();
                String[] ids = value.split("-");
                //将数组转化为liekedlist集合
                LinkedList<String> list = new LinkedList<>(Arrays.asList(ids));
                //判断记录是否在历史中
                if(list.contains(pid)){
                    //浏览过该商品
                    list.remove(pid);
                    list.addFirst(pid);
                }else {
                    //没有浏览过
                    //判断历史记录长度
                    if (list.size()>=6){
                        list.removeLast();
                        list.addFirst(pid);
                    }else {
                        //没有超过
                        list.addFirst(pid);
                    }
                }
                StringBuffer sb = new StringBuffer();
                for (String id : list ){
                    sb.append(id).append("-");
                }
                String idstr = sb.toString().substring(0,sb.length()-1);
                Cookie c = new Cookie("history",idstr);
                c.setMaxAge(7*24*60*60);
                response.addCookie(c);
            }
            return "/jsp/product_info.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //清除历史记录
    public String delCook(HttpServletRequest request,HttpServletResponse response) {
        Cookie c = new Cookie("history","");
        c.setMaxAge(0);
        response.addCookie(c);
        return  "/";
    }
}
