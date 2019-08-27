package com.itheima.store.service;

import com.itheima.store.domain.PageBean;
import com.itheima.store.domain.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductService {
    //查询最新商品
    List<Product> findByNew() throws SQLException;
    //查询热门商品
    List<Product> findByHot() throws SQLException;
    //获取当前分类数据
    PageBean<Product> findByPageCid(String cid, String currPage) throws SQLException;
    //获得商品详情
    Product findBypid(String pid) throws SQLException;
    //查询商品
    PageBean<Product> findByPage(String currPage)throws SQLException;
    //根据pid查询商品和分类
    Product findPCByPid(String pid)throws  Exception;
    //修改商品详情
    void save(Product product)throws  Exception;
    //添加商品
    void addProduct(Product product)throws  Exception;
}
