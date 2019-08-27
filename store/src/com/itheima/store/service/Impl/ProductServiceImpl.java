package com.itheima.store.service.Impl;

import com.itheima.store.dao.Impl.ProductDaoImpl;
import com.itheima.store.dao.ProductDao;
import com.itheima.store.domain.PageBean;
import com.itheima.store.domain.Product;
import com.itheima.store.service.ProductService;
import com.itheima.store.utils.BeanFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: nihaoa
 * \* Date: 2018/7/10
 * \* Time: 18:56
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class ProductServiceImpl implements ProductService {
    //查询最新商品
    @Override
    public List<Product> findByNew() throws SQLException {
        ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
        List<Product> newList = productDao.findByNew();
        return newList;
    }
    //查询热门商品
    @Override
    public List<Product> findByHot() throws SQLException {
        ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
        List<Product> hotlist = productDao.findByHot();
        return hotlist;
    }
    //获取当前分类数据
    @Override
    public PageBean<Product> findByPageCid(String cid, String currPage) throws SQLException {
        PageBean pageBean = new PageBean();
        //设置当前页数
        pageBean.setCurrPage(Integer.parseInt(currPage));
        //设置每页记录数
        pageBean.setPageSize(12);
        //设置当前分类总记录数
        ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
        Integer totalCount = productDao.findCountByCid(cid);
        pageBean.setTotalCount(totalCount);
        //设置总页数
        double to = totalCount;
        Double num = Math.ceil(to/pageBean.getPageSize());
        pageBean.setTotoPage(num.intValue());
        //设置当前页数据
        //设置每页显示数据
        int begin = (Integer.parseInt(currPage)-1)*pageBean.getPageSize();
        List<Product>list = productDao.findPageByCid(cid,begin,pageBean.getPageSize());
        pageBean.setList(list);
        return pageBean;
    }
    //获得商品详情
    @Override
    public Product findBypid(String pid) throws SQLException {
        ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
        Product product = productDao.findByPid(pid);
        // 浏览记录

        return product;
    }
    //查询商品
    @Override
    public PageBean<Product> findByPage(String currPage) throws SQLException {
        PageBean<Product>pageBean = new PageBean<Product>();
        pageBean.setCurrPage(Integer.parseInt(currPage));
        pageBean.setPageSize(10);
        ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
        Integer totalCount = productDao.findProduct();
        pageBean.setTotalCount(totalCount);
        Integer totalPage = ((totalCount % 10)==0) ? (totalCount / 10):(totalCount / 10) + 1;
        pageBean.setTotoPage(totalPage);
        //查询当前页
        Integer begin = (Integer.parseInt(currPage)-1)*pageBean.getPageSize();
        List<Product>list = productDao.findPage(begin,pageBean.getPageSize());
        pageBean.setList(list);
        return pageBean;
    }
    //根据pid获取商品和分类
    @Override
    public Product findPCByPid(String pid) throws Exception {
        ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
        Product product = productDao.findPCByPid(pid);
        return product;
    }
    //修改商品详情
    @Override
    public void save(Product product)throws Exception {
        ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
        productDao.save(product);

    }
    //添加商品
    @Override
    public void addProduct(Product product) throws Exception {
        ProductDao productDao = (ProductDao) BeanFactory.getBean("productDao");
        productDao.addProduct(product);
    }



}