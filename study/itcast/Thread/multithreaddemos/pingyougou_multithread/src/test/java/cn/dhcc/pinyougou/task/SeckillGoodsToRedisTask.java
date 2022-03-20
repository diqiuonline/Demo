package cn.dhcc.pinyougou.task;

import cn.dhcc.pinyougou.mapper.TbSeckillGoodsMapper;
import cn.dhcc.pinyougou.pojo.TbSeckillGoods;
import cn.dhcc.pinyougou.pojo.TbSeckillGoodsExample;
import cn.dhcc.pinyougou.utils.SystemConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 李锦卓
 * 2022/3/19 13:23
 * 1.0
 */
@Component
public class SeckillGoodsToRedisTask {
    @Resource
    private TbSeckillGoodsMapper tbSeckillGoodsMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Scheduled(cron = "0/30 * * * * ?")
    public void importToRedis() {
        System.out.println("定时任务执行了"+new Date());
        // 1 查询合法的秒杀商品数据 状态为有效 status=1 stackCount》0 库存  秒杀开始时间小于当前时间  当前时间小于秒杀结束时间
        TbSeckillGoodsExample example = new TbSeckillGoodsExample();
        TbSeckillGoodsExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1").andStockCountGreaterThan(0)
                .andStartTimeLessThanOrEqualTo(new Date()).andEndTimeGreaterThan(new Date());
        List<TbSeckillGoods> tbSeckillGoods = tbSeckillGoodsMapper.selectByExample(example);
        for (TbSeckillGoods tbSeckillGood : tbSeckillGoods) {
            redisTemplate.boundHashOps(TbSeckillGoods.class.getSimpleName()).put(tbSeckillGood.getId(), tbSeckillGood);
            //为每一个商品创建一个队列 队列中放和库存中相同数据的商品id
            createQueue(tbSeckillGood.getId(),tbSeckillGood.getStockCount());
        }
    }

    private void createQueue(Long id, Integer stockCount) {
        if (stockCount > 0) {
            for (Integer i = 0; i < stockCount; i++) {
                redisTemplate.boundListOps(SystemConst.CONST_SECKILLGOODS_ID_PREFIX + id).leftPush(id);
            }
        }
    }
}
