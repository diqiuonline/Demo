package cn.dhcc.pinyougou.controller;

import cn.dhcc.pinyougou.pojo.TbSeckillGoods;
import cn.dhcc.pinyougou.service.SeckillGoodsService;
import cn.dhcc.pinyougou.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 李锦卓
 * 2022/3/19 15:26
 * 1.0
 */
@RequestMapping("/seckillGoods")
@RestController
public class SeckillGoodsController {
    @Autowired
    private SeckillGoodsService seckillGoodsService;
    @RequestMapping("/findAll")
    public List<TbSeckillGoods> findAll() {
        List<TbSeckillGoods> tbSeckillGoodsList = seckillGoodsService.findAll();
        return tbSeckillGoodsList;
    }


    @RequestMapping("/findOne/{id}")
    public TbSeckillGoods findOne(@PathVariable("id") Long id) {
        TbSeckillGoods tbSeckillGoodsList = seckillGoodsService.findOne(id);

        return tbSeckillGoodsList;
    }
    @RequestMapping("/saveOrder/{id}")
    public Result saveOrder(@PathVariable("id") Long id) {
        String userId = "jiuwelong";
        return seckillGoodsService.saveOrder(id,userId);
    }
}
