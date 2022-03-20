package cn.dhcc.pinyougou.service;

import cn.dhcc.pinyougou.pojo.TbSeckillGoods;
import cn.dhcc.pinyougou.utils.Result;

import java.util.List;

/**
 * 李锦卓
 * 2022/3/19 15:45
 * 1.0
 */
public interface SeckillGoodsService {
    List<TbSeckillGoods> findAll();

    TbSeckillGoods findOne(Long id);

    Result saveOrder(Long id, String userId);
}
