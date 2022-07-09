package com.roncoo.eshop.inventory.service.impl;

import com.roncoo.eshop.inventory.dao.RedisDao;
import com.roncoo.eshop.inventory.mapper.ProductInventoryMapper;
import com.roncoo.eshop.inventory.model.ProductInventory;
import com.roncoo.eshop.inventory.service.ProductInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductInventoryServiceImpl implements ProductInventoryService {
    @Autowired
    private ProductInventoryMapper productInventCntMapper;
    @Autowired
    private RedisDao redisDao;
    @Override
    public void updateProductInventory(ProductInventory productInventory) {
        productInventCntMapper.updateProductInventory(productInventory);
        System.out.println("=======日志========："+Thread.currentThread().getName()+":已修改数据库中的库存，商品id="+productInventory.getProductId()+", 商品库存数量="+productInventory.getInventoryCnt());
    }

    @Override
    public void removeProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:" + productInventory.getProductId();
        redisDao.delete(key);
        System.out.println("=======日志========："+Thread.currentThread().getName()+":已删除redis中的缓存，key="+key);
    }

    @Override
    public ProductInventory findProductInventory(Integer productId) {

        return productInventCntMapper.findProductInventory(productId);
    }

    @Override
    public void setProductInventoryCache(ProductInventory productInventory) {

        redisDao.set("product:inventory:" + productInventory.getProductId(), String.valueOf(productInventory.getInventoryCnt()));
        System.out.println("=======日志========："+Thread.currentThread().getName()+":已更新商品库存的缓存，商品id="+productInventory.getProductId()+", 商品库存数量="+productInventory.getInventoryCnt()+"key="+"product:inventory:" + productInventory.getProductId());
    }

    @Override
    public ProductInventory getProductoryCache(Integer productId) {
        Long inventoryCnt = 0L;
        String key = "product:inventory:" + productId;
        String result = redisDao.get(key);
        if (result != null && !"".equals(result)) {
            inventoryCnt = Long.valueOf(result);
            return new ProductInventory(productId,inventoryCnt);
        }
        return null;
    }
}
