package com.roncoo.eshop.inventory.service.impl;

import com.roncoo.eshop.inventory.request.ProductInventoryCacheRefreshRequest;
import com.roncoo.eshop.inventory.request.ProductInventoryDBUpdateRequest;
import com.roncoo.eshop.inventory.request.Request;
import com.roncoo.eshop.inventory.request.RequsetQueue;
import com.roncoo.eshop.inventory.service.RequestAsyncProcessService;
import com.sun.javafx.css.StyleCacheEntry;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 请求异步处理的service实现
 * 读请求去重优化
 *      如果一个读请求过来，发现前面已经有一个写请求和一个读请求了，那么这个读请求就不用压入队列中了 因为那个写请求肯定会更新数据库，然后那个读请求
 *      肯定会从数据库中读取最新的数据，然后刷新到缓存中 自己hang一会就可以从缓存汇中读取数据了
 */
@Service
public class RequestAsynvProcessServiceImpl implements RequestAsyncProcessService {
    @Override
    public void process(Request request) {
        try {
            //先做读请求的去重
            RequsetQueue requsetQueue = RequsetQueue.getInstance();
            Map<Integer, Boolean> flagMap = requsetQueue.getFlagMap();
            if (request instanceof ProductInventoryDBUpdateRequest) {
                //如果是一个更新数据库的请求那么就将那个productId对应的标识设置为ture
                flagMap.put(request.getProductId(), true);
            } else if (request instanceof ProductInventoryCacheRefreshRequest) {
                //如果是缓存刷新的请求，那么就判断 如果标识不为空，而且是true 就说明之前有一个商品的数据库更新请求
                if (flagMap.get(request.getProductId()) != null && flagMap.get(request.getProductId())) {
                    flagMap.put(request.getProductId(),false);
                }
                //如果是缓存刷新的请求 而且发现标识不为空，而且标识是false 说明前面已经有一个数据库更新请求+缓存刷新请求
                if (flagMap.get(request.getProductId()) != null && !flagMap.get(request.getProductId())) {
                    //对于这种读请求，直接就过滤掉 不要放到后面的内存队列中去
                    return;
                }
            }
            //做请求的路由 根据每个请求的商品id，路由到对应的内存队列中去
            ArrayBlockingQueue<Request> queue = getRoutingQueue(request.getProductId());
            //将请求放入到对饮的队列中 完成路由操作
            queue.put(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取路由到的内存队列
     * @param productId
     * @return
     */
    public ArrayBlockingQueue<Request> getRoutingQueue(Integer productId) {
        RequsetQueue requsetQueue = RequsetQueue.getInstance();
        //先获取productId的hash值
        int h;
        String key = String.valueOf(productId);
        int hash = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
        //对hash取模，将hash值路由到指定的内存队列中 比如内存略列大小8
        //用内存队列的数量对hash值取模之后 结果一定是0~7之间
        //所以任何一个商品id 都会别固定路由到同样的一个内存队列中去的
        int index = (requsetQueue.queueSize() - 1) & hash;
        return requsetQueue.getQueue(index);
    }
}
