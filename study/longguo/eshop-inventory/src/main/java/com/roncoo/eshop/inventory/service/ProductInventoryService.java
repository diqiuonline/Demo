package com.roncoo.eshop.inventory.service;

import com.roncoo.eshop.inventory.model.ProductInventory;

/**
 * 商品库存
 */
public interface ProductInventoryService {
    /**
     * 更新商品库存
     */
    void updateProductInventory(ProductInventory productInventory);

    /**
     * 删除redis中商品库存的缓存
     *
     * @param productInventory
     */
    void removeProductInventoryCache(ProductInventory productInventory);

    /**
     * 根据商品id查询商品库存
     * @param productId
     * @return
     */
    ProductInventory findProductInventory(Integer productId);

    /**
     * 设置商品的库存
     *
     * @param productInventory
     */
    void setProductInventoryCache(ProductInventory productInventory);

    /**
     * 获取商品库存的缓存
     * @param productId
     * @return
     */
    ProductInventory getProductoryCache(Integer productId);
}
