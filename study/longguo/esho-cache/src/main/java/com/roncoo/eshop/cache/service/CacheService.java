package com.roncoo.eshop.cache.service;

import com.roncoo.eshop.cache.model.ProductInfo;
import com.roncoo.eshop.cache.model.ShopInfo;

/**
 * @Author 李锦卓
 * @Description 缓存service接口
 * @Date 2022/7/9 18:54
 * @Version 1.0
 */
public interface CacheService {
    /**
     * 将商品信息保存到本地缓存中
     * @param productInfo
     * @return
     */
    ProductInfo saveLocalCache(ProductInfo productInfo);

    /**
     * 从本地缓存中获取商品信息
     * @param id 商品id
     * @return
     */
    ProductInfo getLocalCatch(Long id);

    /**
     * 将商品信息保存到本地ehcache缓存中
     * @param productInfo
     */
    ProductInfo saveProductInfo2LocalCache(ProductInfo productInfo);

    /**
     * 将店铺信息保存到本地ehcache缓存中
     * @param shopInfo
     */
    ShopInfo saveShopInfo2LocalCache(ShopInfo shopInfo);

    /**
     * 将商品信息保存到redis中
     * @param productInfo
     */
    void saveProductInfo2ReidsCache(ProductInfo productInfo);
    /**
     * 将店铺信息保存到redis中
     * @param shopInfo
     */
    void saveShopInfo2ReidsCache(ShopInfo shopInfo);
    /**
     * 从本地ehcache缓存中获取商品信息
     * @param productId
     * @return
     */
    public ProductInfo getProductInfoFromLocalCache(Long productId);
    /**
     * 从本地ehcache缓存中获取店铺信息
     * @param shopId
     * @return
     */
    public ShopInfo getShopInfoFromLocalCache(Long shopId);


    /**
     * 从redis中获取商品信息
     * @param productId
     */
    public ProductInfo getProductInfoFromReidsCache(Long productId);

    /**
     * 从redis中获取店铺信息
     * @param shopId
     */
    public ShopInfo getShopInfoFromReidsCache(Long shopId);

}
