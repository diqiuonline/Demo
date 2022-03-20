package cn.dhcc.pinyougou.service.impl;

import cn.dhcc.pinyougou.mapper.TbSeckillGoodsMapper;
import cn.dhcc.pinyougou.pojo.TbSeckillGoods;
import cn.dhcc.pinyougou.pojo.TbSeckillOrder;
import cn.dhcc.pinyougou.service.SeckillGoodsService;
import cn.dhcc.pinyougou.thread.OrderCreateThread;
import cn.dhcc.pinyougou.utils.IdWorker;
import cn.dhcc.pinyougou.utils.OrderRecord;
import cn.dhcc.pinyougou.utils.Result;
import cn.dhcc.pinyougou.utils.SystemConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * 李锦卓
 * 2022/3/19 15:45
 * 1.0
 */
@Service
@Transactional
public class SeckillGoodsServiceImpl implements SeckillGoodsService {
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public List<TbSeckillGoods> findAll() {
        return redisTemplate.boundHashOps(TbSeckillGoods.class.getSimpleName()).values();
    }

    @Override
    public TbSeckillGoods findOne(Long id) {
        return (TbSeckillGoods) redisTemplate.boundHashOps(TbSeckillGoods.class.getSimpleName()).get(id);
    }


    @Autowired
    private IdWorker idWorker;
    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    private Executor executor;
    @Autowired
    private OrderCreateThread orderCreateThread;
    @Override
    public Result saveOrder(Long id, String userId) {
        //从用户的set集合中判断用户是否已下单
        Boolean member = redisTemplate.boundSetOps(SystemConst.CONST_USER_ID_PREFIX + id).isMember(userId);
        //如果正在拍段或者未支付的 提示用户你正在排队或者有订单未支付
        if (member) {
            return new Result(false, "对不起，您正在排队等待支付，请尽快支付");
        }
        //从队列中获取秒杀商品的id
        id = (Long) redisTemplate.boundListOps(SystemConst.CONST_SECKILLGOODS_ID_PREFIX+id).rightPop();
        //判断商品是否存在或库存是否<=0
        if (StringUtils.isEmpty(id)) {
            return new Result(false, "已售罄");
        }
        //将用户放入用户集合
        redisTemplate.boundSetOps(SystemConst.CONST_USER_ID_PREFIX + id).add(userId);
        //创建orderRecord对象 记录用户下单信息 用户id 商品id 放到orderRecord队列中
        OrderRecord orderRecord = new OrderRecord(id, userId);
        redisTemplate.boundListOps(OrderRecord.class.getSimpleName()).leftPush(orderRecord);
        //通过线程池启动线程处理orderRecord中的数据 返回成功
        executor.execute(orderCreateThread);
        return new Result(true,"秒杀成功");
    }
}
