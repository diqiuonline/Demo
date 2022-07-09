package com.roncoo.eshop.inventory.controller;

import com.roncoo.eshop.inventory.model.ProductInventory;
import com.roncoo.eshop.inventory.request.ProductInventoryCacheRefreshRequest;
import com.roncoo.eshop.inventory.request.ProductInventoryDBUpdateRequest;
import com.roncoo.eshop.inventory.request.Request;
import com.roncoo.eshop.inventory.service.ProductInventoryService;
import com.roncoo.eshop.inventory.service.RequestAsyncProcessService;
import com.roncoo.eshop.inventory.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品库存controller
 * 可能某个数据，在数据库里面压根就没有 那么这个读请求是不需要放入内存队列中的，而且读请求在controller那一次就可以直接放回了 不需要等待
 * 如果数据库中都没有，就说明，内存队列里面如果没有数据库跟新请求的话 一个读请求过来，就可以认为是数据库里压根就没有数据
 * 如果缓存中没数据 就两种情况 第一种数据库中没有数据缓存也肯定没有数据，第二个是数据库中更新操作过来了先删除缓存中的数据此时缓存是空的 数据库是有的
 * 但是的话呢 我们之前做了读请求去重的优化 用了一个flag 只要前面有数据库更新操作，flag就肯定是存在的 你只不过可以更具flag的值 判断你前面是读请求还是写请求
 * 但是如果flag压根就没有 就说明这个数据  无论是写请求还是读请求 都没有过，
 * 那这个时候过来的读请求 发现flag是null 就可以认为数据库里也肯定是空的了 那就不会去读取了
 * 或者说 我们也可以认为每个商品有一个最最初始的库存 但是应为最初是的库存肯定会同步到缓存中取得 有一种特殊的情况 商品库存本来在redis中是有缓存的
 * 但是因为redis内存满了，给干掉了 但是此时数据库中是右值的 那么在这种情况下 可能就是之前的没有任何读请求和写请求的flag的值 此时还是要从数据库中重新加载一次数据到缓存中
 *
 */
@Controller
public class ProductInventoryController {
    @Autowired
    ProductInventoryService productInventoryService;
    @Autowired
    RequestAsyncProcessService requestAsyncProcessService;

    /**
     * 更新商品库存
     *
     * @param productInventory
     * @return
     */
    @RequestMapping("/updateProductInventory")
    @ResponseBody
    public Response updateProductInventory(ProductInventory productInventory) {
        System.out.println("=======日志========："+Thread.currentThread().getName()+":接收到更新商品库存的请求，商品id="+productInventory.getProductId()+", 商品库存=："+productInventory.getInventoryCnt());
        Response response = null;
        try {
            Request request = new ProductInventoryDBUpdateRequest(productInventory, productInventoryService);
            requestAsyncProcessService.process(request);
            response = new Response(Response.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response(Response.FAILURE);
        }
        return response;
    }



    /**
     * 获取商品库存
     *
     * @param productId
     * @return
     */
    @RequestMapping("/getProductInventory")
    @ResponseBody
    public ProductInventory getProductInventory(Integer productId) {
        System.out.println("=======日志========："+Thread.currentThread().getName()+":接收到商品库存的读请求，商品id="+productId);


        ProductInventory productInventory = null;
        try {
            Request request = new ProductInventoryCacheRefreshRequest(productId, productInventoryService,false);
            requestAsyncProcessService.process(request);
            //将请求扔给service异步去处理以后 就需要while（true）一会儿 在这里hang住
            //去尝试等待前面有商品库存更新的操作，同时缓存刷新的操作，将最新的数据刷新到缓存中
            long startTime = System.currentTimeMillis();
            //System.out.println("=======日志========："+Thread.currentThread().getName()+":startTime="+startTime);
            long endTime = 0l;
            long waitTIme = 0l;
            //等待超过200ms没有从缓存中获取结果
            while (true) {
                //System.out.println("=======日志========："+Thread.currentThread().getName()+":waitTime="+waitTIme);
                if (waitTIme > 200){
                    break;
                }
                //尝试去redis中读取一次商品的缓存数据
                productInventory = productInventoryService.getProductoryCache(productId);
                if (!StringUtils.isEmpty(productInventory)) {
                    System.out.println("=======日志========："+Thread.currentThread().getName()+":在200ms内读取到了redis中的库存缓存，商品id="+productId+", 商品库存数量="+productInventory.getInventoryCnt());
                    return productInventory;
                }
                //如果没有读取到结果 那么等待一段时间
                else {
                    Thread.sleep(20);
                    endTime = System.currentTimeMillis();
                    //System.out.println("=======日志========："+Thread.currentThread().getName()+":endTime="+endTime);
                    waitTIme = endTime - startTime;


                }
            }
            System.out.println("=======日志========："+Thread.currentThread().getName()+":在200ms内没有读取到了redis中的库存缓存，商品id="+productId+"直接从数据库读取");
            //直接冲数据库中尝试读取数据
            productInventory = productInventoryService.findProductInventory(productId);
            if (!StringUtils.isEmpty(productId)) {
                //将缓存刷新一下
                request = new ProductInventoryCacheRefreshRequest(productId, productInventoryService,true);
                requestAsyncProcessService.process(request);
                //productInventoryService.setProductInventoryCache(productInventory);
                //代码运行到这里 只有三种情况
                //1 就是说 上一次也是读请求 数据刷入了redis 但是redis lRU算法给清理掉了 标志位还是false 所以下一个读请求是从缓存中拿不到数据到 在放一个读request进队列 让数据去刷新一下
                // 2 可能在200ms内 就是读请求在队列中一直积压着 没有等待到它执行（扩容机器） 所以就直接查库 然后给队列中赛进去一个刷新缓存的请求
                // 3 数据库中本身就没有，缓存穿透 穿透redis 请求达到mysql
                return productInventory;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ProductInventory(productId, -1L);
    }
}
