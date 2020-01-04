package com.itheima.store.service.Impl;

import com.itheima.store.dao.CategoryDao;
import com.itheima.store.domain.Category;
import com.itheima.store.service.CategoryService;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.web.servlet.AdminCategoryServlet;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.sql.SQLException;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    //查询所有分类
    @Override
    public List<Category> findAll() throws SQLException {
       /* CategoryDao categoryDao = new CategoryDaoImpl();
        List<Category> list = categoryDao.findAll();*/
        //CategoryService cateGoryService = new CategoryServiceImpl();
        //读取配置文件
        CacheManager cacheManager = CacheManager.create(CategoryServiceImpl.class.getClassLoader().getResourceAsStream("ehcache.xml"));
        //从配置文件中获取缓存去
        Cache cache = cacheManager.getCache("categoryCache");
        //判断缓存中是否有list集合
        Element element = cache.get("list");
        List<Category> list = null;
        if(element == null){
            //缓存中没有数据
            System.out.println("缓存中没有数据");
            CategoryDao categoryDao = (CategoryDao) BeanFactory.getBean("categoryDao");
            list = categoryDao.findAll();
            element = new Element("list",list);
            cache.put(element);
        }else {
            System.out.println("缓存中有数据");
            list = (List<Category>) element.getObjectValue();
        }
        return list;
    }
    //添加分类
    @Override
    public void save(Category category) throws SQLException {
        CategoryDao categoryDao = (CategoryDao) BeanFactory.getBean("categoryDao");
        categoryDao.save(category);
        //清除缓存
        CacheManager cacheManager = CacheManager.create(AdminCategoryServlet.class.getClassLoader().getResourceAsStream("ehcache.xml"));
        //从配置文件中获取缓存
        Cache cache = cacheManager.getCache("categoryCache");
        cache.remove("list");
    }
    //根据id查询分类
    @Override
    public Category findByCid(String cid) throws SQLException {
        CategoryDao categoryDao = (CategoryDao) BeanFactory.getBean("categoryDao");
        return categoryDao.findByCid(cid);
    }
    //修改分类
    @Override
    public void update(String cid,String name) throws SQLException {
        CategoryDao categoryDao = (CategoryDao) BeanFactory.getBean("categoryDao");
        categoryDao.update(cid,name);
        //清除缓存
        CacheManager cacheManager = CacheManager.create(AdminCategoryServlet.class.getClassLoader().getResourceAsStream("ehcache.xml"));
        //从配置文件中获取缓存
        Cache cache = cacheManager.getCache("categoryCache");
        cache.remove("list");
    }
    //删除分类
    @Override
    public void delete(String cid) throws SQLException {
        //找到该cid的product更改为null
        CategoryDao categoryDao = (CategoryDao) BeanFactory.getBean("categoryDao");
        categoryDao.updateProdectCid(cid);
        //清除缓存
        CacheManager cacheManager = CacheManager.create(AdminCategoryServlet.class.getClassLoader().getResourceAsStream("ehcache.xml"));
        //从配置文件中获取缓存
        Cache cache = cacheManager.getCache("categoryCache");
        cache.remove("list");
    }
}
