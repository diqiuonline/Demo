package com.roncoo.eshop.inventory.thread;

import com.roncoo.eshop.inventory.request.ProductInventoryCacheRefreshRequest;
import com.roncoo.eshop.inventory.request.ProductInventoryDBUpdateRequest;
import com.roncoo.eshop.inventory.request.Request;
import com.roncoo.eshop.inventory.request.RequsetQueue;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

/**
 * 执行请求的工作线程
 */
public class RequestProcessorThread implements Callable<Boolean> {
    /**
     * 自己监控的内存队列
     */
    private ArrayBlockingQueue<Request> queue;

    public RequestProcessorThread(ArrayBlockingQueue<Request> queue) {
        this.queue = queue;
    }
    @Override
    public Boolean call() throws Exception {
        try {
            while (true) {
                //ArrayBlockQueue
                //Blockingg 就是说明 如果队列满了 或者是空的都会在执行操作的时候阻塞住
                Request request = queue.take();
                boolean forceRefresh = request.isForceRefresh();
                if (!forceRefresh) {
                    //先做读请求的去重
                    RequsetQueue requsetQueue = RequsetQueue.getInstance();
                    Map<Integer, Boolean> flagMap = requsetQueue.getFlagMap();
                    if (request instanceof ProductInventoryDBUpdateRequest) {
                        //如果是一个更新数据库的请求那么就将那个productId对应的标识设置为ture
                        flagMap.put(request.getProductId(), true);
                        System.out.println("=======日志========：" + Thread.currentThread().getName() + ":工作线程处理请求，商品id=" + request.getProductId());

                        //执行request操作
                        request.process();
                    } else if (request instanceof ProductInventoryCacheRefreshRequest) {

                        Boolean flag = flagMap.get(request.getProductId());

                        //如果flag值nul
                        //如果flag是null
                        if (StringUtils.isEmpty(flag)) {
                            flagMap.put(request.getProductId(), false);
                            System.out.println("=======日志========：" + Thread.currentThread().getName() + ":工作线程处理请求，商品id=" + request.getProductId());

                            //执行request操作
                            request.process();
                        }
                        //如果是缓存刷新的请求 那么判断 如果表示不为空 而且是true 就说明之前有过一个这个商品的数据库更新请求
                        if (!StringUtils.isEmpty(flag) && flag) {
                            flagMap.put(request.getProductId(), false);
                            System.out.println("=======日志========：" + Thread.currentThread().getName() + ":工作线程处理请求，商品id=" + request.getProductId());

                            //执行request操作
                            request.process();
                        }
                        //如果是缓存刷新的请求 而且发现标识不为空，而且标识是false 说明前面已经有一个数据库更新请求+缓存刷新请求
                        if (!StringUtils.isEmpty(flag) && !flag) {
                            //对于这种读请求，直接就过滤掉 不要放到后面的内存队列中去
                            System.out.println("=======日志========：" + Thread.currentThread().getName() + ": 重复读请求 直接访问redis");
                        }

                    }
                } else {
                    System.out.println("=======日志========：" + Thread.currentThread().getName() + ":工作线程处理请求，商品id=" + request.getProductId());

                    //执行request操作
                    request.process();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
