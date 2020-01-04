package com.itheima.store.web.servlet;

import com.itheima.store.domain.Category;
import com.itheima.store.domain.Product;
import com.itheima.store.service.ProductService;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.UUIDUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "addProductServlet",urlPatterns = "/addProductServlet")
public class addProductServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //创建product对象
            Product product = new Product();
            //创建一个磁盘工厂
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            //获得核心解析类
            ServletFileUpload fileUpload = new ServletFileUpload(diskFileItemFactory);
            fileUpload.setHeaderEncoding("UTF-8"); //解决中文文件名乱码问题
            //解析request 返回list集合
            List<FileItem> list = fileUpload.parseRequest(request);
            //获得每个部分 遍历 存入一个map集合中
            Map<String ,String> map = new HashMap<String, String>();
            String fileName = null;
            for (FileItem fileItem:list){
                //判断普通项和文件上传项
                if ( fileItem.isFormField()){
                    //普通项
                    String name = fileItem.getFieldName();
                    String value = fileItem.getString("UTF-8");//解决普通项的中文乱码
                    map.put(name,value);
                }else {
                    //文件上传项
                    //获得文件名
                    fileName = fileItem.getName();
                    //获得文件输入流
                    InputStream is = fileItem.getInputStream();
                    //获得文件上传路劲
                    String path = this.getServletContext().getRealPath("/products/1");
                    //String path = "E:/Study";
                    //输出流
                    OutputStream os = new FileOutputStream(path+"/"+fileName);
                    int len = 0;
                    byte[] b = new byte[1024 * 8];
                    while ((len = is.read(b))!= -1){
                        os.write(b,0,len);
                    }
                    is.close();
                    os.close();
                }
            }
            //封装数据
            BeanUtils.populate(product,map);
            product.setPid(UUIDUtils.getUUID());
            product.setPdate(new Date());
            product.setPflag(0);
            product.setPimage("products/1/"+fileName);
            Category category = new Category();
            category.setCid(map.get("cid"));
            product.setCategory(category);
            //保存数据库
            ProductService productService = (ProductService) BeanFactory.getBean("productService");
            productService.addProduct(product);
            //页面跳转
            response.sendRedirect(request.getContextPath()+"/AdminProductServlet?method=findByPage&currPage=1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
