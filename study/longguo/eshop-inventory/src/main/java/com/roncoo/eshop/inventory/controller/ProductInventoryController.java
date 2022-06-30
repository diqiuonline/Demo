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
        ProductInventory productInventory = null;
        try {
            Request request = new ProductInventoryCacheRefreshRequest(productId, productInventoryService);
            requestAsyncProcessService.process(request);
            //将请求扔给service异步去处理以后 就需要while（true）一会儿 在这里hang住
            //去尝试等待前面有商品库存更新的操作，同时缓存刷新的操作，将最新的数据刷新到缓存中
            long startTime = System.currentTimeMillis();
            long endTime = 0l;
            long waitTIme = 0l;
            //等待超过200ms没有从缓存中获取结果
            while (true) {
                if (waitTIme > 200){
                    break;
                }
                //尝试去redis中读取一次商品的缓存数据
                productInventory = productInventoryService.getProductoryCache(productId);
                if (!StringUtils.isEmpty(productInventory)) {
                    return productInventory;
                }
                //如果没有读取到结果 那么等待一段时间
                else {
                    Thread.sleep(20);
                    endTime = System.currentTimeMillis();
                    waitTIme = endTime - startTime;


                }
            }
            //直接冲数据库中尝试读取数据
            productInventory = productInventoryService.findProductInventory(productId);
            if (!StringUtils.isEmpty(productId)) {
                return productInventory;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ProductInventory(productId, -1L);
    }
}
