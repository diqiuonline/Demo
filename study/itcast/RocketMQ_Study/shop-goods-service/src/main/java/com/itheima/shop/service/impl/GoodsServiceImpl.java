package com.itheima.shop.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.api.IGoodsService;
import com.itheima.constant.ShopCode;
import com.itheima.entity.Result;
import com.itheima.exception.CastException;
import com.itheima.shop.mapper.TradeGoodsMapper;
import com.itheima.shop.mapper.TradeGoodsNumberLogMapper;
import com.itheima.shop.pojo.TradeGoods;
import com.itheima.shop.pojo.TradeGoodsNumberLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
@Service(interfaceClass = IGoodsService.class)
public class GoodsServiceImpl implements IGoodsService {
    @Autowired
    private TradeGoodsMapper goodsMapper;
    @Autowired
    private TradeGoodsNumberLogMapper goodsNumberLogMapper;
    @Override
    public TradeGoods findOne(Long goodsId) {
        if (StringUtils.isEmpty(goodsId)) {
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        TradeGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
        return goods;
    }

    /**
     * 扣减库存
     *
     * @param goodsNumberLog
     * @return
     */
    @Override
    public Result reduceGoodsNum(TradeGoodsNumberLog goodsNumberLog) {
        if (StringUtils.isEmpty(goodsNumberLog) || StringUtils.isEmpty(goodsNumberLog.getGoodsNumber()) ||
                StringUtils.isEmpty(goodsNumberLog.getGoodsId()) || StringUtils.isEmpty(goodsNumberLog.getOrderId()) ||
                StringUtils.isEmpty(goodsNumberLog.getGoodsNumber().intValue() <= 0)) {
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        TradeGoods goods = goodsMapper.selectByPrimaryKey(goodsNumberLog.getGoodsId());
        if (goods.getGoodsNumber() < goodsNumberLog.getGoodsNumber()) {
            //库存不足
            CastException.cast(ShopCode.SHOP_GOODS_NUM_NOT_ENOUGH);
        }
        //减库存
        goods.setGoodsNumber(goods.getGoodsNumber() - goodsNumberLog.getGoodsNumber());
        goodsMapper.updateByPrimaryKey(goods);
        //记录库存操作的日志
        goodsNumberLog.setGoodsNumber(-(goodsNumberLog.getGoodsNumber()));
        goodsNumberLog.setLogTime(new Date());
        goodsNumberLogMapper.insert(goodsNumberLog);
        return new Result(ShopCode.SHOP_SUCCESS.getSuccess(),ShopCode.SHOP_SUCCESS.getMessage());
    }
}
