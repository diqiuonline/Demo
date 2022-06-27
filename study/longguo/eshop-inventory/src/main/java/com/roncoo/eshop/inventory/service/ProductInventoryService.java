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

    ProductInventory findProductInventory(Integer productId);

    void setProductInventoryCache(ProductInventory productInventory);
}
