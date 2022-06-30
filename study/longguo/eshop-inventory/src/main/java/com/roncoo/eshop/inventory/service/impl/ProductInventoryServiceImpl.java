package com.roncoo.eshop.inventory.service.impl;

import com.roncoo.eshop.inventory.dao.RedisDao;
import com.roncoo.eshop.inventory.mapper.ProductInventoryMapper;
import com.roncoo.eshop.inventory.model.ProductInventory;
import com.roncoo.eshop.inventory.service.ProductInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.Oneway;

@Service
public class ProductInventoryServiceImpl implements ProductInventoryService {
    @Autowired
    private ProductInventoryMapper productInventCntMapper;
    @Autowired
    private RedisDao redisDao;
    @Override
    public void updateProductInventory(ProductInventory productInventory) {
        productInventCntMapper.updateProductInventory(productInventory);
    }

    @Override
    public void removeProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:" + productInventory.getProductId();
        redisDao.delete(key);
    }

    @Override
    public ProductInventory findProductInventory(Integer productId) {

        return productInventCntMapper.findProductInventory(productId);
    }

    @Override
    public void setProductInventoryCache(ProductInventory productInventory) {
        redisDao.set("product:inventory:" + productInventory.getProductId(),String.valueOf(productInventory.getProductId()));
    }

    @Override
    public ProductInventory getProductoryCache(Integer productId) {
        Long inventoryCnt = 0L;
        String key = "product:inventory:" + productId;
        String result = redisDao.get(key);
        if (result != null && "".endsWith(result)) {
            inventoryCnt = Long.valueOf(result);
            return new ProductInventory(productId,inventoryCnt);
        }
        return null;
    }
}
