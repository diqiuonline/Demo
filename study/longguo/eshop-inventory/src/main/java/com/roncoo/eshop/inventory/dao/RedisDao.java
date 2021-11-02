package com.roncoo.eshop.inventory.dao;

public interface RedisDao {
    void set(String key, String value);

    String get(String key);
}
