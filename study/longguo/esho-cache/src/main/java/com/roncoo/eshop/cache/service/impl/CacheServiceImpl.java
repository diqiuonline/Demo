package com.roncoo.eshop.cache.service.impl;

import com.roncoo.eshop.cache.model.ProductInfo;
import com.roncoo.eshop.cache.service.CacheService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Author 李锦卓
 * @Description 缓存服务实现层
 * @Date 2022/7/9 18:57
 * @Version 1.0
 */
@Service
public class CacheServiceImpl implements CacheService {
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
}
