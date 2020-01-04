package com.itheima.store.dao.Impl;

import com.itheima.store.dao.CategoryDao;
import com.itheima.store.domain.Category;
import com.itheima.store.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao {
    //查询所有分类
    @Override
    
    public List<Category> findAll() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select * from category";
        List<Category> list = queryRunner.query(sql, new BeanListHandler<Category>(Category.class));
        return list;
    }
    //添加分类
    @Override
    public void save(Category category) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "insert into category values (?,?)";
        queryRunner.update(sql,category.getCid(),category.getCname());
    }
    //根据id查询分类
    @Override
    public Category findByCid(String cid) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select * from category where cid = ?";
        Category category = queryRunner.query(sql, new BeanHandler<Category>(Category.class), cid);
        return category;
    }
    //修改分类
    @Override
    public void update(String cid,String cname) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "update category set cname = ? where cid = ?";
        queryRunner.update(sql,cname,cid);

    }
    //删除商品分类
    @Override
    public void updateProdectCid(String cid) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "update product set cid = null where cid = ?";
        queryRunner.update(sql,cid);
        sql = "delete from category where cid = ?";
        queryRunner.update(sql,cid);
    }
}
