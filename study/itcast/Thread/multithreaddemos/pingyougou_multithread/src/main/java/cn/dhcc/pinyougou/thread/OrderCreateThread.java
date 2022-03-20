package cn.dhcc.pinyougou.thread;

import cn.dhcc.pinyougou.mapper.TbSeckillGoodsMapper;
import cn.dhcc.pinyougou.pojo.TbSeckillGoods;
import cn.dhcc.pinyougou.pojo.TbSeckillOrder;
import cn.dhcc.pinyougou.utils.IdWorker;
import cn.dhcc.pinyougou.utils.OrderRecord;
import cn.dhcc.pinyougou.utils.SystemConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 李锦卓
 * 2022/3/20 22:25
 * 1.0
 */
@Controller
public class OrderCreateThread implements Runnable{

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public void run() {
        OrderRecord orderRecord = (OrderRecord) redisTemplate.boundListOps(OrderRecord.class.getSimpleName()).rightPop();
        if (!StringUtils.isEmpty(orderRecord)) {

            TbSeckillGoods tbSeckillGoods = (TbSeckillGoods) redisTemplate.boundHashOps(TbSeckillGoods.class.getSimpleName()).get(orderRecord.getId());
            //商品不存在 活库存小于0 返回失败 提示已售罄
            //生成秒杀订单 将订单保存到redis
            TbSeckillOrder tbSeckillOrder = new TbSeckillOrder();
            tbSeckillOrder.setUserId(orderRecord.getUserId());
            tbSeckillOrder.setSellerId(tbSeckillGoods.getSellerId());
            tbSeckillOrder.setSeckillId(idWorker.nextId());
            tbSeckillOrder.setMoney(tbSeckillGoods.getCostPrice());
            tbSeckillOrder.setCreateTime(new Date());
            tbSeckillOrder.setStatus("0");
            redisTemplate.boundHashOps(TbSeckillOrder.class.getSimpleName()).put(orderRecord.getUserId(), tbSeckillOrder);
            synchronized (OrderCreateThread.class) {
                tbSeckillGoods = (TbSeckillGoods) redisTemplate.boundHashOps(TbSeckillGoods.class.getSimpleName()).get(orderRecord.getId());
                //秒杀商品库存量-1
                tbSeckillGoods.setStockCount(tbSeckillGoods.getStockCount() - 1);
                //判断库存量是否小于等于0
                if (tbSeckillGoods.getStockCount() <= 0) {
                    //是 将秒杀商品更新到数据库 删除秒杀商品缓存
                    seckillGoodsMapper.updateByPrimaryKey(tbSeckillGoods);
                    redisTemplate.boundHashOps(TbSeckillGoods.class.getSimpleName()).delete(orderRecord.getId());
                } else {
                    //否 将秒杀商品更新到缓存 返回成功
                    redisTemplate.boundHashOps(TbSeckillGoods.class.getSimpleName()).put(orderRecord.getId(),tbSeckillGoods);

                }
            }

        }


    }
}
