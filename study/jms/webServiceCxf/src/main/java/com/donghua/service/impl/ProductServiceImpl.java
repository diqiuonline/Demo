package com.donghua.service.impl;

import com.donghua.mapper.ProductMapper;
import com.donghua.pojo.Product;
import com.donghua.pojo.ProductExample;
import com.donghua.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: 李锦卓
 * Time: 2018/11/14 10:41
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    public List<Product> getProductList() {
        ProductExample productExample = new ProductExample();
        List<Product> products = productMapper.selectByExample(productExample);
        return products;
    }
}