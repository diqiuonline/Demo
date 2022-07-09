package com.roncoo.eshop.inventory.request;

import com.roncoo.eshop.inventory.model.ProductInventory;
import com.roncoo.eshop.inventory.service.ProductInventoryService;

/**
 * 重新加载商品库存的缓存
 */
public class ProductInventoryCacheRefreshRequest implements Request{

    private Integer productId;

    private ProductInventoryService productInventoryService;

    /**
     * 时候强制刷新缓存
     * @param productId
     * @param productInventoryService
     */
    private boolean forceRefresh;

    public ProductInventoryCacheRefreshRequest(Integer productId, ProductInventoryService productInventoryService,boolean forceRefresh) {
        this.productId = productId;
        this.productInventoryService = productInventoryService;
        this.forceRefresh = forceRefresh;
    }

    @Override
    public void process( ) {
        //从数据库中查询最新的商品库存
        ProductInventory productInventory = productInventoryService.findProductInventory(productId);
        System.out.println("=======日志========："+Thread.currentThread().getName()+":已查询到最新的商品库存数量，商品id="+productId+", 商品库存数量="+productInventory.getInventoryCnt());
        //将最新的商品库存数量刷新到redis中
        productInventoryService.setProductInventoryCache(productInventory);
    }

    @Override
    public Integer getProductId() {
        return productId;
    }

    public void setForceRefresh(boolean forceRefresh) {
        this.forceRefresh = forceRefresh;
    }

    public boolean isForceRefresh() {
        return forceRefresh;
    }
}
