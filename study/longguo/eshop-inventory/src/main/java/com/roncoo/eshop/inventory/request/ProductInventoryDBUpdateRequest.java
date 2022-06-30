package com.roncoo.eshop.inventory.request;

import com.roncoo.eshop.inventory.dao.RedisDao;
import com.roncoo.eshop.inventory.model.ProductInventory;
import com.roncoo.eshop.inventory.service.ProductInventoryService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 比如说一个商品发生了交易，那么就要修改这个商品对应的库存
 * 此时就会发送请求过来 要求修改库存 那么这个可能就是所谓的data update request 数据更新请求
 * <p>
 * （1）删除缓存
 * （2）更新数据库
 */
public class ProductInventoryDBUpdateRequest implements Request {
    /**
     * 商品库存
     */
    private ProductInventory productInventory;
    @Autowired
    private ProductInventoryService productInventoryService;

    public ProductInventoryDBUpdateRequest(ProductInventory productInventory, ProductInventoryService productInventoryService) {
        this.productInventory = productInventory;
        this.productInventoryService = productInventoryService;
    }

    @Override
    public void process( ) {
        //删除redis缓存
        productInventoryService.removeProductInventoryCache(productInventory);
        //修改数据库中缓存
        productInventoryService.updateProductInventory(productInventory);

    }

    @Override
    public Integer getProductId() {
        return productInventory.getProductId();
    }
}
