package com.itheima.shop.mq;

import com.alibaba.fastjson.JSON;
import com.itheima.constant.ShopCode;
import com.itheima.entity.MQEntity;
import com.itheima.shop.mapper.TradeGoodsMapper;
import com.itheima.shop.mapper.TradeGoodsNumberLogMapper;
import com.itheima.shop.mapper.TradeMqConsumerLogMapper;
import com.itheima.shop.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author 李锦卓
 * @version 1.0
 * @date 2020/1/12 22:45
 */
@Component
@RocketMQMessageListener(topic = "${mq.order.topic}",
        consumerGroup = "${mq.order.consumer.group.name}", messageModel = MessageModel.BROADCASTING)
@Slf4j
public class CancelMQListener implements RocketMQListener<MessageExt> {

    @Autowired
    private TradeMqConsumerLogMapper mqConsumerLogMapper;
    @Value("${mq.order.consumer.group.name}")
    private String groupName;
    @Autowired
    private TradeGoodsMapper goodsMapper;
    @Autowired
    private TradeGoodsNumberLogMapper goodsNumberLogMapper;
    @Override
    public void onMessage(MessageExt messageExt) {
        String msgId = null;
        String tags = null;
        String keys = null;
        String body = null;
        try {
            //1 解析消息内容
             msgId = messageExt.getMsgId();
             tags = messageExt.getTags();
             keys = messageExt.getKeys();
             body = new String(messageExt.getBody(), "utf-8");
            log.info("接受消息成功");
            //2 查询消息消费记录
            TradeMqConsumerLogKey primaryKey = new TradeMqConsumerLogKey();
            primaryKey.setMsgTag(tags);
            primaryKey.setMsgKey(keys);
            primaryKey.setGroupName(groupName);
            TradeMqConsumerLog mqConsumerLog = mqConsumerLogMapper.selectByPrimaryKey(primaryKey);
            if (!StringUtils.isEmpty(messageExt)) {
                //3 判断如果消费过
                // 3.1 获得消息处理状态
                Integer status = mqConsumerLog.getConsumerStatus();
                //处理过  ..返回
                if (ShopCode.SHOP_MQ_MESSAGE_STATUS_SUCCESS.getCode().intValue() == status.intValue()) {
                    log.info("消息"+ msgId+"已经处理过");
                    return;
                }
                //正在处理 ..返回
                if (ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode().intValue() == status.intValue()) {
                    log.info("消息" + msgId + "正在处理");
                    return;
                }
                //处理失败
                if (ShopCode.SHOP_MQ_MESSAGE_STATUS_FAIL.getCode().intValue() == status.intValue()) {
                    //获得消息处理次数
                    Integer times = mqConsumerLog.getConsumerTimes();
                    if (times > 3) {
                        log.info("消息" + msgId + "消息处理超过三次  不能再进行处理了");
                        return;
                    }
                    //乐观锁处理
                    mqConsumerLog.setConsumerStatus(ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode());
                    TradeMqConsumerLogExample example = new TradeMqConsumerLogExample();
                    TradeMqConsumerLogExample.Criteria criteria = example.createCriteria();
                    criteria.andMsgTagEqualTo(mqConsumerLog.getMsgTag());
                    criteria.andMsgKeyEqualTo(mqConsumerLog.getMsgKey());
                    criteria.andGroupNameEqualTo(groupName);
                    criteria.andConsumerTimesEqualTo(mqConsumerLog.getConsumerTimes());
                    int i = mqConsumerLogMapper.updateByExampleSelective(mqConsumerLog, example);
                    if (i <= 0) {
                        //未修改成功，其他线程并发修改
                        log.info("并发修改，稍后处理");
                    }
                }
            } else {
                //4 判断如果没有消费过
                mqConsumerLog = new TradeMqConsumerLog();
                mqConsumerLog.setMsgTag(tags);
                mqConsumerLog.setMsgKey(keys);
                mqConsumerLog.setConsumerStatus(ShopCode.SHOP_MQ_MESSAGE_STATUS_PROCESSING.getCode());
                mqConsumerLog.setMsgBody(body);
                mqConsumerLog.setMsgId(msgId);
                mqConsumerLog.setConsumerTimes(0);
                //将消息处理的信息 添加到数据库
                mqConsumerLogMapper.insert(mqConsumerLog);
            }
            //5 回退库存
            MQEntity mqEntity = JSON.parseObject(body, MQEntity.class);
            Long goodsId = mqEntity.getGoodsId();
            TradeGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
            goods.setGoodsNumber(goods.getGoodsNumber() + mqEntity.getGoodsNum());
            goodsMapper.updateByPrimaryKey(goods);
            //记录库存操作的日志
            TradeGoodsNumberLog goodsNumberLog = new TradeGoodsNumberLog();
            goodsNumberLog.setOrderId(mqEntity.getOrderId());
            goodsNumberLog.setGoodsId(mqEntity.getGoodsId());
            goodsNumberLog.setGoodsNumber(mqEntity.getGoodsNum());
            goodsNumberLog.setLogTime(new Date());
            goodsNumberLogMapper.insert(goodsNumberLog);
            //6 将消息的处理状态改为成功
            mqConsumerLog.setConsumerStatus(ShopCode.SHOP_MQ_MESSAGE_STATUS_SUCCESS.getCode());
            mqConsumerLog.setConsumerTimestamp(new Date());
            mqConsumerLogMapper.updateByPrimaryKey(mqConsumerLog);
            log.info("回退库存成功");
        } catch (Exception e) {
            e.printStackTrace();
            TradeMqConsumerLogKey primaryKey = new TradeMqConsumerLogKey();
            primaryKey.setMsgTag(tags);
            primaryKey.setMsgKey(keys);
            primaryKey.setGroupName(groupName);
            TradeMqConsumerLog mqConsumerLog = mqConsumerLogMapper.selectByPrimaryKey(primaryKey);
            if (StringUtils.isEmpty(mqConsumerLog)) {
                //数据库未有记录 处理第一次失败
                mqConsumerLog = new TradeMqConsumerLog();
                mqConsumerLog = new TradeMqConsumerLog();
                mqConsumerLog.setMsgTag(tags);
                mqConsumerLog.setMsgKey(keys);
                mqConsumerLog.setConsumerStatus(ShopCode.SHOP_MQ_MESSAGE_STATUS_FAIL.getCode());
                mqConsumerLog.setMsgBody(body);
                mqConsumerLog.setMsgId(msgId);
                mqConsumerLog.setConsumerTimes(1);
                mqConsumerLogMapper.insert(mqConsumerLog);
            } else {
                mqConsumerLog.setConsumerTimes(mqConsumerLog.getConsumerTimes() + 1);
                mqConsumerLogMapper.updateByPrimaryKeySelective(mqConsumerLog);
            }
        }


    }
}
