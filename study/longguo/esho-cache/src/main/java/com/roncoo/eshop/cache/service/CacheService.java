package com.roncoo.eshop.cache.service;

import com.roncoo.eshop.cache.model.ProductInfo;

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
}
