package com.itheima.store.dao;

import com.itheima.store.domain.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDao {
    //查询最新商品
    List<Product> findByNew() throws SQLException;
    //查询热门商品
    List<Product> findByHot() throws SQLException;
    //查询当前分类总记录数
    Integer findCountByCid(String cid) throws SQLException;
    //查询当前页数据
    List<Product> findPageByCid(String cid, int begin, Integer pageSize) throws SQLException;
    //获得商品详情
    Product findByPid(String pid) throws SQLException;
    //获得总记录数
    Integer findProduct() throws SQLException;
    //获得管理页面商品数据
    List<Product> findPage(Integer begin, Integer pageSize) throws SQLException;
    //根据pid查询商品和分类
    Product findPCByPid(String pid) throws Exception ;
    //修改商品详情
    void save(Product product)throws Exception ;
    //添加商品
    void addProduct(Product product)throws Exception ;
}
