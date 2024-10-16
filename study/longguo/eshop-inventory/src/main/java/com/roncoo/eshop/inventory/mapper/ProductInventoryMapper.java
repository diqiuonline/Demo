package com.roncoo.eshop.inventory.mapper;

import com.roncoo.eshop.inventory.model.ProductInventory;
import org.apache.ibatis.annotations.Param;

//库存数量mapper
public interface ProductInventoryMapper {
    /**
     * 更新库存数量
     * @param productInventory

     */
    public void updateProductInventory(ProductInventory productInventory);
    ProductInventory findProductInventory(@Param("productId") Integer productId);
}
