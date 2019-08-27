package com.itheima.store.service;

import com.itheima.store.domain.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryService {
    //查询所有分类
    List<Category> findAll()throws SQLException;
    //添加分类
    void save(Category category)throws SQLException;
    //根据id查询分类
    Category findByCid(String cid)throws SQLException;
    //修改分类
    void update(String cid, String name)throws SQLException;
    //删除分类
    void delete(String cid)throws SQLException;
}
