package com.roncoo.eshop.cache.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.cache.model.ProductInfo;
import com.roncoo.eshop.cache.model.ShopInfo;
import com.roncoo.eshop.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

/**
 * @Author 李锦卓
 * @Description 缓存服务实现层
 * @Date 2022/7/9 18:57
 * @Version 1.0
 */
@Service
public class CacheServiceImpl implements CacheService {
    @Autowired
    private JedisCluster jedisCluster;
    public static final String CACHE_NAME = "local";

    @Override
    @Cacheable(value = CACHE_NAME,key = "'key_'+#productInfo.getId()")
    public ProductInfo saveLocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    @Override
    @Cacheable(value = CACHE_NAME,key = "'key_'+#id")
    public ProductInfo getLocalCatch(Long id) {
        return null;
    }


    @Override
    @Cacheable(value = CACHE_NAME,key = "'product_info_'+#productInfo.getId()")
    public ProductInfo saveProductInfo2LocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    @Override
    @Cacheable(value = CACHE_NAME,key = "'shop_info_'+#shopInfo.getId()")
    public ShopInfo saveShopInfo2LocalCache(ShopInfo shopInfo) {
        return shopInfo;
    }

    @Override
    public void saveProductInfo2ReidsCache(ProductInfo productInfo) {
        String key = "product_info_" + productInfo.getId();
        jedisCluster.set(key, JSONObject.toJSONString(productInfo));
    }

    @Override
    public void saveShopInfo2ReidsCache(ShopInfo shopInfo) {
        String key = "shop_info_" + shopInfo.getId();
        jedisCluster.set(key, JSONObject.toJSONString(shopInfo));

    }
    /**
     * 从本地ehcache缓存中获取商品信息
     * @param productId
     * @return
     */
    @Cacheable(value = CACHE_NAME, key = "'product_info_'+#productId")
    public ProductInfo getProductInfoFromLocalCache(Long productId) {
        return null;
    }
    /**
     * 从本地ehcache缓存中获取店铺信息
     * @param shopId
     * @return
     */
    @Cacheable(value = CACHE_NAME, key = "'shop_info_'+#shopId")
    public ShopInfo getShopInfoFromLocalCache(Long shopId) {
        return null;
    }

    /**
     * 从redis中获取商品信息
     * @param productId
     */
    public ProductInfo getProductInfoFromReidsCache(Long productId) {
        String key = "product_info_" + productId;
        String json = jedisCluster.get(key);
        return JSONObject.parseObject(json, ProductInfo.class);
    }

    /**
     * 从redis中获取店铺信息
     * @param shopId
     */
    public ShopInfo getShopInfoFromReidsCache(Long shopId) {
        String key = "shop_info_" + shopId;
        String json = jedisCluster.get(key);
        return JSONObject.parseObject(json, ShopInfo.class);
    }

}
