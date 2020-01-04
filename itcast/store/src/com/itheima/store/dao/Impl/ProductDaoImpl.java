package com.itheima.store.dao.Impl;

import com.itheima.store.dao.ProductDao;
import com.itheima.store.domain.Category;
import com.itheima.store.domain.Product;
import com.itheima.store.utils.JDBCUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: nihaoa
 * \* Date: 2018/7/10
 * \* Time: 18:59
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class ProductDaoImpl implements ProductDao {
    //查询最新商品
    @Override
    public List<Product> findByNew() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select * from product where pflag = ?  order by pdate desc limit ?";
        List<Product> list = queryRunner.query(sql, new BeanListHandler<Product>(Product.class), 0, 9);
        return list;
    }
    //查询热门商品
    @Override
    public List<Product> findByHot() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select * from product where pflag = ? and is_hot = ? order by pdate desc limit ?";
        List<Product> list = queryRunner.query(sql, new BeanListHandler<Product>(Product.class), 0,1, 9);
        return list;
    }
    //查询当前分类总记录数
    @Override
    public Integer findCountByCid(String cid) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select count(*) from product where cid = ? and pflag = ?";
        Long count = (Long) queryRunner.query(sql,new ScalarHandler(),cid,0);
        return count.intValue();
    }
    //查询当前页数据
    @Override
    public List<Product> findPageByCid(String cid, int begin, Integer pageSize) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select * from product where pflag = ? and cid = ? order by pdate desc limit ?,?";
        List<Product> list = queryRunner.query(sql, new BeanListHandler<Product>(Product.class), 0, cid, begin, pageSize);
        return list;
    }
    //获得商品详情
    @Override
    public Product findByPid(String pid) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select * from product where pid = ?";
        Product product = queryRunner.query(sql, new BeanHandler<Product>(Product.class), pid);
        return product;
    }
    //获得总记录数
    @Override
    public Integer findProduct() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select count(*) from product where pflag = ?";
        Long lo = (Long) queryRunner.query(sql,new ScalarHandler(),0);
        return lo.intValue();
    }
    //获得管理页面商品数据
    @Override
    public List<Product> findPage(Integer begin, Integer pageSize) throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "select * from product where pflag = ? order by pdate desc limit ?,?";
        List<Product> list = queryRunner.query(sql, new BeanListHandler<Product>(Product.class), 0, begin, pageSize);
        return list;
    }
    //根据pid获得商品和分类
    @Override
    public Product findPCByPid(String pid) throws Exception {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "SELECT * FROM product p,category c WHERE p.cid = c.cid AND p.pid = ?";
        Map<String, Object> map = queryRunner.query(sql, new MapHandler(), pid);
        Product product = new Product();
        Category category = new Category();
        BeanUtils.populate(product,map);
        BeanUtils.populate(category,map);
        product.setCategory(category);
        return product;
    }
    //修改商品详情
    @Override
    public void save(Product product) throws Exception {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "update product set pname = ?,market_price = ?,shop_price = ?,pimage = ?,pdate = ?,is_hot = ?,pdesc = ?,pflag = ?,cid = ? where pid = ?";
        Object[] obj = {product.getPname(),product.getMarket_price(),product.getShop_price(),product.getPimage(),product.getPdate(),product.getIs_hot(),product.getPdesc(),product.getPflag(),product.getCategory().getCid(),product.getPid()};
        queryRunner.update(sql,obj);
    }
    //添加商品
    @Override
    public void addProduct(Product product) throws Exception {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils.getDataSource());
        String sql = "insert into product values (?,?,?,?,?,?,?,?,?,?)";
        Object[] obj = {product.getPid(),product.getPname(),product.getMarket_price(),product.getShop_price(),product.getPimage(),product.getPdate(),product.getIs_hot(),product.getPdesc(),product.getPflag(),product.getCategory().getCid()};
        queryRunner.update(sql,obj);
    }

}